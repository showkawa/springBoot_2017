package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @EnableEurekaClient
 *
 * 服务注册端  将服务注册在eureka上
 * 提供者项目创建方式与注册中心服务器相同，并且做以下修改：
 *
 * 1. 需要将@EnableEurekaServer改为@EnableEurekaClient。
 *
 * 2. spring-cloud-starter-netflix-eureka-server依赖修改为spring-cloud-starter-netflix-eureka-client。
 */
@SpringBootApplication
//@EnableEurekaClient
public class SpbBrianQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpbBrianQueryServiceApplication.class, args);
    }

}

