package com.kawa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 *@EnableEurekaServer
 * 	eureka注册中心
 */
@SpringBootApplication
@EnableEurekaServer
public class SpbCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpbCoreApplication.class, args);
	}
	
	
}
