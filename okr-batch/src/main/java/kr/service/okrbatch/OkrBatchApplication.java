package kr.service.okrbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class OkrBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrBatchApplication.class, args);
	}

}
