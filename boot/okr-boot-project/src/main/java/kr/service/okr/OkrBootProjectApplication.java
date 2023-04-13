package kr.service.okr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EntityScan({
	"kr.service.okr",
})
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OkrBootProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrBootProjectApplication.class, args);
	}

}
