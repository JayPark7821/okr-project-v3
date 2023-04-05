package kr.service.okr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class OkrCloudDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrCloudDiscoveryApplication.class, args);
	}

}
