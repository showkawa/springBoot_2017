package com.kawa.spb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpbQueryService2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpbQueryService2Application.class, args);
    }

}

