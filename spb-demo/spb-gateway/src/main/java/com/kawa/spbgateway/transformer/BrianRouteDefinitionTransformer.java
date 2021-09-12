package com.kawa.spbgateway.transformer;

import com.kawa.spbgateway.config.ApiKeysConfiguration;
import com.kawa.spbgateway.route.BrianRouteDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.kawa.spbgateway.content.Contents.*;

@Slf4j
public class BrianRouteDefinitionTransformer {
    private String defaultRewritePathRegexp = "^/api/(?<domain>[a-zA-Z-]*)/v(?<version>[0-9])/(?<path>.*)";
    private String defaultRewritePathReplacement = "/v$\\{version}/$\\{path}";
    private String extendRewritePathRegexp = "^/api/(?<region>[a-zA-Z-]*)/(?<domain>[a-zA-Z-]*)/v(?<version>[0-9])/(?<path>.*)";
    private String extendRewritePathReplacement = "/v$\\{version}/$\\{path}";

    private String defaultRewriteDomainRegexp = "^/api/(?<domain>[a-zA-Z-]*)/v.+/.*";
    private String defaultRewriteDomainReplacement = "https://$\\{domain}.free.beeceptor.com";
    private String extendRewriteDomainRegexp = "^/api/(?<region>[a-zA-Z-]*)/(?<domain>[a-zA-Z-]*)/v.+/.*";
    private String extendRewriteDomainReplacement = "https://$\\{domain}-$\\{region}.free.beeceptor.com";

    private List<String> default1FAValues = Arrays.asList("pwd", "sms", "gAuth");

    private List<String> default2FAValues = Arrays.asList("pwd+sms", "sms+gAuth");

    private ApiKeysConfiguration apiKeys;

    public RouteDefinition transform(BrianRouteDefinition brianRouteDefinition, String uri) {
        // add ConfigGatewayFilter
        FilterDefinition configFilter = new FilterDefinition();
        configFilter.setName(CONFIG_GATEWAY_FILTER_CLASS_NAME);
        HashMap<String, String> configArgs = new HashMap<>();

        var apiKeyString = brianRouteDefinition.getApiKeys().stream().map(ak -> apiKeys.getValue(ak)).collect(Collectors.toList()).toString();
        configArgs.put(GATEWAY_CONFIG_CLASS_API_KEYS, apiKeyString.substring(1, apiKeyString.length() - 1));

        configArgs.put(GATEWAY_CONFIG_CLASS_AUTH, default1FAValues.toString());
        if (Objects.nonNull(brianRouteDefinition.getAuths()) &&
                brianRouteDefinition.getAuths().size() > 0) {
            configArgs.put(GATEWAY_CONFIG_CLASS_AUTH, brianRouteDefinition.getAuths().toString());
        }
        configFilter.setArgs(configArgs);
        brianRouteDefinition.getFilters().add(configFilter);

        if (StringUtils.hasText(uri)) {
            brianRouteDefinition.setUri(URI.create(uri));
            // set route id
            setRouteId(brianRouteDefinition);
        }
        long count = brianRouteDefinition.getFilters().stream()
                .filter(filterDefinition -> filterDefinition.getName().equals(REWRITE_GATEWAY_FILTER_CLASS_NAME))
                .count();
        // get path value from Prediction config
        var path = getPathString(brianRouteDefinition);
        log.info(">>>>>>>>>> route path: {}", path);
        var replacement = defaultRewriteDomainReplacement.replace("$\\", "$");
        Pattern pattern = Pattern.compile(defaultRewriteDomainRegexp);
        Matcher defaultMatcher = pattern.matcher(path);
        if (defaultMatcher.matches()) {
            String newDomain = defaultMatcher.replaceAll(replacement);
            log.info(">>>>>>>>>>  redefine the path {{}} and new domain {{}}", path, newDomain);
            if (Objects.isNull(brianRouteDefinition.getUri())) {
                brianRouteDefinition.setUri(URI.create(newDomain));
                // set route id
                setRouteId(brianRouteDefinition);
            }
            // add RewritePathGatewayFilter
            if (count < 1L) {
                addRewriteFilter(brianRouteDefinition, defaultRewritePathRegexp, defaultRewritePathReplacement);
            }
            return brianRouteDefinition;
        }

        var replacementExt = extendRewriteDomainReplacement.replace("$\\", "$");
        Pattern patternExt = Pattern.compile(extendRewriteDomainRegexp);
        Matcher defaultExtMatcher = patternExt.matcher(path);
        if (defaultExtMatcher.matches()) {
            String newDomain = defaultExtMatcher.replaceAll(replacementExt);
            if (Objects.isNull(brianRouteDefinition.getUri())) {
                brianRouteDefinition.setUri(URI.create(newDomain));
                // set route id
                setRouteId(brianRouteDefinition);
            }
            // add RewritePathGatewayFilter
            if (count < 1L) {
                addRewriteFilter(brianRouteDefinition, extendRewritePathRegexp, extendRewritePathReplacement);
            }
            return brianRouteDefinition;
        }
        if (Objects.isNull(brianRouteDefinition.getUri())) {
            brianRouteDefinition.setUri(URI.create(FALL_BACK_URI + path));
            // set route id
            setRouteId(brianRouteDefinition);
        }
        return brianRouteDefinition;
    }

    private void setRouteId(BrianRouteDefinition customizedRouteDefinition) {
        String url = customizedRouteDefinition.getUri().toString();
        customizedRouteDefinition.setId(String.format("%s@%s", url, customizedRouteDefinition.hashCode()));
    }

    private void addRewriteFilter(BrianRouteDefinition customizedRouteDefinition, String rewritePathRegexp, String rewritePathReplacement) {
        FilterDefinition rewriteFilter = new FilterDefinition();
        rewriteFilter.setName(REWRITE_GATEWAY_FILTER_CLASS_NAME);
        HashMap<String, String> rewriteFilterArgs = new HashMap<>();
        rewriteFilterArgs.put(REWRITE_GATEWAY_FILTER_REGEXP, rewritePathRegexp);
        rewriteFilterArgs.put(REWRITE_GATEWAY_FILTER_REPLACEMENT, rewritePathReplacement);
        rewriteFilter.setArgs(rewriteFilterArgs);
        customizedRouteDefinition.getFilters().add(rewriteFilter);
    }

    private String getPathString(BrianRouteDefinition customizedRouteDefinition) {
        for (PredicateDefinition predicateDefinition : customizedRouteDefinition.getPredicates()) {
            if (PREDICATE_PATH.equals(predicateDefinition.getName())) {
                var firstKey = predicateDefinition.getArgs().keySet().iterator().next();
                return predicateDefinition.getArgs().get(firstKey);
            }
        }
        return FALL_BACK_URI;
    }

}
