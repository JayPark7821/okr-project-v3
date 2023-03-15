package kr.jay.okrver3.util;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import kr.jay.okrver3.domain.user.auth.TokenVerifyProcessor;
import kr.jay.okrver3.infrastructure.user.auth.TokenVerifier;

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
