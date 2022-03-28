package com.kawa.bb.overwrite;

import reactor.core.publisher.Mono;

import java.util.Objects;

public class BrianService {

    public OvUserService userService;

    public String type = "1FA";

//    public BrianService(OvUserService userService) {
//        this.userService = userService;
//    }

    public Mono<String> testUs(boolean canDo) {
        if (Objects.isNull(type) || canDo == false) {
            return Mono.just("empty");
        }
        return Mono.just(userService.toString());
    }
}
