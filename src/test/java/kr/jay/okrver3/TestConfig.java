package kr.jay.okrver3;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import kr.jay.okrver3.interfaces.user.auth.TokenVerifier;

@TestConfiguration
public class TestConfig {

	@Primary
	@Bean
	public TokenVerifier tokenVerifier() {
		return new FakeTokenVerifier();
	}
}
