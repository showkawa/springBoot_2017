package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@Slf4j
public class AutoRefreshEnvService implements EnvironmentAware {

    @Autowired
    private ContextRefresher contextRefresher;

    private Environment environment;

    @Scheduled(cron = "0 */1 * * * ?")
    private void autoRefresh() {
        log.info(">>>>>>>>>> auto refresh the apikey");
        randomChangeBean();
        log.info(">>>>>>>>>> auto refresh the apikey completed");
    }

    private void randomChangeBean() {
        if (environment instanceof ConfigurableEnvironment) {
            environment = (ConfigurableEnvironment) environment;
            MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
            PropertySource<?> apikeyPropertySource = propertySources.get("apikey");
            String originalKey = (String) apikeyPropertySource.getProperty("gateway.route.api.test001");
            propertySources.remove("apikey");
            HashMap<String, Object> apiKeyParams = new HashMap<>();
            String newKey = UUID.randomUUID().toString().replace("-", "");
            apiKeyParams.put("gateway.route.api.test001", newKey);
            log.info(">>>>>>>>>>> apikey: { originalKey:{}, newKey:{} }", originalKey, newKey);
            propertySources.addFirst(new MapPropertySource("apikey", apiKeyParams));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
