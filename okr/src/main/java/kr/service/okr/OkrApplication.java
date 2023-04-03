package kr.service.okr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
	scanBasePackages = {
		"kr.service.okr"
	}
)
@EnableDiscoveryClient
public class OkrApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrApplication.class, args);
	}

}
