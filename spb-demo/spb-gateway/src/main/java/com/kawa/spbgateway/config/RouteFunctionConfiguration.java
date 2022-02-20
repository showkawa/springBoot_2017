package com.kawa.spbgateway.config;

import com.kawa.spbgateway.service.RolesRouteFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * RouterFunction of reactive demo
 */
@Configuration
@EnableR2dbcRepositories
public class RouteFunctionConfiguration {

    @Bean
    public RouterFunction<ServerResponse> rolesRouter(RolesRouteFunction routerFunction) {
        return RouterFunctions.nest(RequestPredicates.path("/role"),
                RouterFunctions.route(RequestPredicates.GET("/all"), routerFunction::findAllRoles)
                        .andRoute(RequestPredicates.POST("/save")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), routerFunction::saveRole)
                        .andRoute(RequestPredicates.POST("/remove/{userName}"), routerFunction::removeRole)
                        .andRoute(RequestPredicates.POST("/modify/{userName}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), routerFunction::updateRole)
        );
    }
}
