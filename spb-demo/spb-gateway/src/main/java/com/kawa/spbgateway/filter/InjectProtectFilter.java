package com.kawa.spbgateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.hdiv.config.validations.DefaultValidationParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.codec.CodecProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.xml.sax.SAXException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Component
public class InjectProtectFilter implements GlobalFilter, Ordered {

    private List<Pattern> hdivRules = new ArrayList<>();
    DefaultValidationParser parser = new DefaultValidationParser();
    private ObjectMapper objectMapper;
    private List<HttpMessageReader<?>> messageReaders;

    public InjectProtectFilter(@Value("${gateway.inject-protect.file:}") String validationFilePath, ObjectMapper objectMapper,
                               CodecConfigurer codecConfigurer, CodecProperties codecProperties) {
        log.info(">>>>>>>>>> InjectProtectFilter");
        if (StringUtils.hasText(validationFilePath)) {
            readValidations(validationFilePath, parser);
        } else {
            parser.readDefaultValidations();
        }
        List<Map<DefaultValidationParser.ValidationParam, String>> validations = parser.getValidations();
        validations.forEach(val -> {
            String regex = val.get(DefaultValidationParser.ValidationParam.REGEX);
            hdivRules.add(Pattern.compile(regex));
        });
        this.objectMapper = objectMapper;
        this.messageReaders = fetchMessageReaders(codecConfigurer, codecProperties);

    }

    private List<HttpMessageReader<?>> fetchMessageReaders(CodecConfigurer codecConfigurer, CodecProperties codecProperties) {
        PropertyMapper propertyMapper = PropertyMapper.get();
        CodecConfigurer.DefaultCodecs defaultCodecs = codecConfigurer.defaultCodecs();
        propertyMapper
                .from(codecProperties.getMaxInMemorySize())
                .whenNonNull()
                .asInt(DataSize::toBytes)
                .to(defaultCodecs::maxInMemorySize);
        return codecConfigurer.getReaders();
    }

    /**
     * prevent external entities operate
     *
     * @param validationFilePath
     * @param parser
     */
    private void readValidations(String validationFilePath, DefaultValidationParser parser) {
        try (var fis = new FileInputStream(validationFilePath)) {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/xternal-parameter-entities", false);
            spf.setFeature("http://xml.org/sax/features/disallow-doctype-decl", true);
            SAXParser sp = spf.newSAXParser();
            sp.parse(fis, parser);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long contentLength = exchange.getRequest().getHeaders().getContentLength();
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        List<String> headers = exchange.getRequest().getHeaders().values().stream().flatMap(list -> list.stream()).collect(Collectors.toList());
        log.info(">>>>>>>>>> InjectProtectFilter-filter request headers: {}", headers);
        if (contentLength > 0 && (MediaType.APPLICATION_JSON.equals(contentType))) {
            return DataBufferUtils
                    .join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> validateJson(exchange, chain, dataBuffer, headers));
        }
        validateParamList(headers);
        return chain.filter(exchange);
    }

    private boolean validateParam(String paramStr, Pattern rule) {
        Matcher matcher = rule.matcher(paramStr);
        return matcher.matches();
    }

    private Mono<? extends Void> validateJson(ServerWebExchange exchange, GatewayFilterChain chain,
                                              DataBuffer dataBuffer, List<String> params) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);

        Flux<DataBuffer> cacheFlux = Flux.defer(() -> {
            DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(bytes);
            DataBufferUtils.retain(wrap);
            return Mono.just(wrap);
        });
        ServerHttpRequest serverHttpRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return cacheFlux;
            }
        };

        ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
        return ServerRequest.create(serverWebExchange, messageReaders)
                .bodyToMono(String.class)
                .doOnNext(bodyStr -> {
                    try {
                        JsonNode jsonNode = objectMapper.readValue(bodyStr, JsonNode.class);
                        add2ParamList(jsonNode, params);
                    } catch (JsonProcessingException e) {
                        log.info(">>>>>>>>> invalid json body: {}", bodyStr);
                    }
                    validateParamList(params);
                }).then(chain.filter(serverWebExchange));
    }


    private void add2ParamList(JsonNode jsonNode, List<String> params) {
        if (jsonNode.isObject()) {
            ObjectNode objNode = (ObjectNode) jsonNode;
            objNode.fields().forEachRemaining(val -> add2ParamList(val.getValue(), params));
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            arrayNode.forEach(node -> add2ParamList(node, params));
        } else {
            params.add(jsonNode.asText());
        }
    }

    private void validateParamList(List<String> params) {
        Iterator<String> paramIterator = params.iterator();
        stop:
        while (paramIterator.hasNext()) {
            String param = paramIterator.next();
            Iterator<Pattern> ruleIterator = hdivRules.iterator();
            while (ruleIterator.hasNext()) {
                Pattern rule = ruleIterator.next();
                if (validateParam(param, rule)) {
                    log.error(">>>>>>>>>> hit the security rule, param:{} rule:{}", param, rule);
                    // throw exception
                    break stop;
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 99;
    }
}
