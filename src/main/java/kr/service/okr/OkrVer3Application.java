package kr.service.okr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OkrVer3Application {

	public static void main(String[] args) {
		SpringApplication.run(OkrVer3Application.class, args);
	}

}
