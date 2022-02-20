package com.kawa.spbgateway.service;


import com.kawa.spbgateway.domain.Roles;
import com.kawa.spbgateway.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class RolesRouteFunction {

    @Autowired
    private RolesRepository rolesRepository;

    public Mono<ServerResponse> findAllRoles(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(rolesRepository.findAll(), Roles.class);
    }

    @Transactional
    public Mono<ServerResponse> saveRole(ServerRequest request) {
        Mono<Roles> roleMono = request.bodyToMono(Roles.class);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(rolesRepository.saveAll(roleMono), Roles.class);
    }

    @Transactional
    public Mono<ServerResponse> removeRole(ServerRequest request) {
        String userName = request.pathVariable("userName");
        return rolesRepository.findById(userName).flatMap(r -> rolesRepository.deleteById(userName).then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Transactional
    public Mono<ServerResponse> updateRole(ServerRequest request) {
        String userName = request.pathVariable("userName");
        Mono<Roles> roleMono = request.bodyToMono(Roles.class);
        return rolesRepository.findById(userName).flatMap(r -> {
                    Flux<Roles> rolesFlux = rolesRepository.saveAll(roleMono);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(rolesFlux, Roles.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());

    }

}