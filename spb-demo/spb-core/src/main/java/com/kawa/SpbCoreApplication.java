package com.kawa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class SpbCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpbCoreApplication.class, args);
	}
	
	
}
