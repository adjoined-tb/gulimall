package me.adjoined.gulimall.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class GulimallDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallDiscoveryApplication.class, args);
	}

}

