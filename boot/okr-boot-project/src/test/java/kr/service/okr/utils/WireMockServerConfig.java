package kr.service.okr.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.github.tomakehurst.wiremock.WireMockServer;

@TestConfiguration
public class WireMockServerConfig {

	@Bean(initMethod = "start", destroyMethod = "stop")
	public WireMockServer mockUserApi() {
		return new WireMockServer(8080);
	}
}
