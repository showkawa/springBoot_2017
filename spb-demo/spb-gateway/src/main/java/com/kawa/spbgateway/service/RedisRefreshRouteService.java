package com.kawa.spbgateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class RedisRefreshRouteService implements ApplicationEventPublisherAware, ApplicationRunner {

    @Autowired
    private RedisDynamicRouteService repository;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;


    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    private void loadRoutes(){
        log.info(">>>>>>>>>> init routes from redis <<<<<<<<<<");
        Flux<RouteDefinition> routeDefinitions = repository.getRouteDefinitions();
        routeDefinitions.subscribe(r-> {
            routeDefinitionWriter.save(Mono.just(r)).subscribe();
        });
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void add(RouteDefinition routeDefinition){
        Assert.notNull(routeDefinition.getId(),"routeDefinition is can not be null");
        repository.save(Mono.just(routeDefinition)).subscribe();
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("---------------------------------------------------");
    }

    public void update(RouteDefinition routeDefinition){
        Assert.notNull(routeDefinition.getId(),"routeDefinition is can not be null");
        repository.delete(Mono.just(routeDefinition.getId())).subscribe();
        routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
        repository.save(Mono.just(routeDefinition)).subscribe();
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }


    public void delete(String id){
        Assert.notNull(id,"routeDefinition is can not be null");
        repository.delete(Mono.just(id)).subscribe();
        routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadRoutes();
    }
}
