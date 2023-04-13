package kr.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
	"kr.service.oauth",
	"kr.service.user",
	"kr.service.jwt",
})
@EntityScan({
	"kr.service.okr",
	"kr.service.user"
})
@EnableFeignClients("kr.service.oauth")
@EnableDiscoveryClient
@SpringBootApplication
public class OkrBootUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrBootUserApplication.class, args);
	}

}
