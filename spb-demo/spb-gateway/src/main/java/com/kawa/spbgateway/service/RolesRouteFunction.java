package com.kawa.spbgateway.service;


import com.kawa.spbgateway.domain.Roles;
import com.kawa.spbgateway.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
public class RolesRouteFunction {

    @Autowired
    private RoleRepository repository;

    public Mono<ServerResponse> findAllRoles(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(), Roles.class);
    }

}
