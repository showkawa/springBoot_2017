package com.kawa.spbgateway.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Slf4j
@Configuration
@PropertySource(name = "apikey", value = "file:${gateway.apikey.prop.path:${spring.config.location}apikey.properties}")
@ConfigurationProperties("gateway.route")
public class ApiKeysConfiguration implements EnvironmentAware {

    private Environment environment;

    private Map<String, Object> apiKey = new HashMap<>();

    public String getValue(String key) {
        return (String) apiKey.get(key);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    private void autoRefresh() {
        log.info(">>>>>>>>>> auto refresh apikey <<<<<<<<<<");
        randomChangeBean();
        log.info(">>>>>>>>>> auto refresh apikey completed <<<<<<<<<<");
    }

    private void randomChangeBean() {
        if (environment instanceof ConfigurableEnvironment) {
            environment = (ConfigurableEnvironment) environment;
            MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
            apiKey.keySet().forEach(key -> {
                String newVal = UUID.randomUUID().toString().replace("-", "");
                log.info(">>>>>>>>>>> key:{}, value:{ oldValue:{}, newValue:{} }", key, apiKey.get(key), newVal);
                apiKey.put(key, newVal);
            });
            propertySources.addLast(new MapPropertySource("apikey", apiKey));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
