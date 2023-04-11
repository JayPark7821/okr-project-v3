package kr.service.okr.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import kr.service.oauth.TokenVerifier;
import kr.service.okr.application.user.SocialTokenVerifyProcessor;

@TestConfiguration
public class TestConfig {

	@Primary
	@Bean
	public TokenVerifier tokenVerifier() {
		return new FakeTokenVerifier();
	}

	@Primary
	@Bean
	public SocialTokenVerifyProcessor tokenVerifyProcessor() {
		return new FakeTokenProcessorImpl(tokenVerifier());
	}
}
