package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class FileRefreshRouteService implements ApplicationEventPublisherAware, CommandLineRunner {

    @Autowired
    private FileDynamicRouteService refreshRoute;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    private void autoRefresh() {
        refreshRoute();
    }

    private synchronized void refreshRoute() {
        try {
            log.info(">>>>>>>>>> start refresh route <<<<<<<<<<");
            if (refreshRoute.refreshRoutes()) {
                applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            }
        } catch (IOException e) {
            log.error("Refresh route failed :{}", e.getMessage());
            throw new IllegalStateException("Refresh route failed :{}", e);
        }
    }

    @Override
    public void run(String... args) {
        refreshRoute();
    }
}
