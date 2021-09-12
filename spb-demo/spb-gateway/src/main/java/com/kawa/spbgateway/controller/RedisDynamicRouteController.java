package com.kawa.spbgateway.controller;

import com.kawa.spbgateway.service.RedisRefreshRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

//@RestController
@RequestMapping("/local")
public class RedisDynamicRouteController {

    @Autowired
    private RedisRefreshRouteService dynamicRouteService;

    @PostMapping("/add")
    public Mono<ResponseEntity<String>> create(@RequestBody RouteDefinition entity) {
        dynamicRouteService.add(entity);
        return Mono.just(new ResponseEntity<>("save success", HttpStatus.OK));
    }

    @PostMapping("/update")
    public Mono<ResponseEntity<String>> update(@RequestBody RouteDefinition entity) {
        dynamicRouteService.update(entity);
        return Mono.just(new ResponseEntity<>("update success", HttpStatus.OK));
    }

    @PostMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        dynamicRouteService.delete(id);
        return Mono.just(new ResponseEntity<>("delete success", HttpStatus.OK));
    }

}
