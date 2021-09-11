package com.kawa.spbgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpbGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpbGatewayApplication.class, args);
	}

}
