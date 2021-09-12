package com.kawa.spbgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

import static com.kawa.spbgateway.content.Contents.*;

@Slf4j
@Component
public class BrianConfigGatewayFilterFactory extends AbstractGatewayFilterFactory<BrianConfigGatewayFilterFactory.Config> {

    public BrianConfigGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            initExchangeAttr(config, exchange);
            return chain.filter(exchange);
        }, 120);
    }

    private void initExchangeAttr(Config config, ServerWebExchange exchange) {
        if (Objects.nonNull(config.getAuths())) {
            exchange.getAttributes().put(GATEWAY_CONFIG_CLASS_AUTH, config.getAuths());
        }
        if (Objects.requireNonNull(config.getApiKeys()).size() > 0) {
            exchange.getAttributes().put(GATEWAY_CONFIG_CLASS_API_KEYS, config.getApiKeys());
        }
    }

    public static class Config {
        private String[] auths;
        private List<String> apiKeys;

        public String[] getAuths() {
            return auths;
        }

        public void setAuths(String[] auths) {
            this.auths = auths;
        }

        public List<String> getApiKeys() {
            return apiKeys;
        }

        public void setApiKeys(List<String> apiKeys) {
            this.apiKeys = apiKeys;
        }
    }
}
