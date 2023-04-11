package kr.service.okr.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import kr.service.oauth.SocialTokenVerifier;
import kr.service.okr.processor.SocialTokenVerifyProcessor;

@TestConfiguration
public class TestConfig {

	@Primary
	@Bean
	public SocialTokenVerifier tokenVerifier() {
		return new FakeSocialTokenVerifier();
	}

	@Primary
	@Bean
	public SocialTokenVerifyProcessor tokenVerifyProcessor() {
		return new FakeTokenProcessorImpl(tokenVerifier());
	}
}
