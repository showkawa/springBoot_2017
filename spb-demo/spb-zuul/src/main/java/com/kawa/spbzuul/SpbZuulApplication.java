package com.kawa.spbzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class SpbZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpbZuulApplication.class, args);
    }

}
