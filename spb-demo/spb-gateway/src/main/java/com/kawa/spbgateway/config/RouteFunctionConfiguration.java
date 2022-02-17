package com.kawa.spbgateway.config;

import com.kawa.spbgateway.service.RolesRouteFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * RouterFunction of reactive demo
 */
@Configuration
@EnableR2dbcRepositories(basePackages = {"com.kawa.spbgateway.repository"})
public class RouteFunctionConfiguration {

    @Bean
    public RouterFunction<ServerResponse> rolesRouter(RolesRouteFunction routerFunction) {
        return RouterFunctions.nest(RequestPredicates.path("/rt"),
                RouterFunctions.route(RequestPredicates.GET("/all"), routerFunction::findAllRoles));
    }
}
