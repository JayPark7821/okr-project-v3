package kr.service.okr.util;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import kr.service.okr.domain.user.auth.TokenVerifyProcessor;
import kr.service.okr.infrastructure.user.auth.TokenVerifier;

@TestConfiguration
public class TestConfig {

	@Primary
	@Bean
	public TokenVerifier tokenVerifier() {
		return new FakeTokenVerifier();
	}

	@Primary
	@Bean
	public TokenVerifyProcessor tokenVerifyProcessor() {
		return new FakeTokenProcessorImpl(tokenVerifier());
	}
}
