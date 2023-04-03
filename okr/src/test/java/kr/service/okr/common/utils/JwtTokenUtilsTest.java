package kr.service.okr.common.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

class JwtTokenUtilsTest {

	private static final String EAMIL = "test@test.com";
	private static final String SECRET_KEY = "secretKey-test-okr-project-jwt-token";
	private static final Long EXPIRE_TIME = 10000000000L;

	@Test
	void generateToken_and_verify() throws Exception {
		String jwt = JwtTokenUtils.generateToken(EAMIL, SECRET_KEY, EXPIRE_TIME);
		assertThat(JwtTokenUtils.getEmail(jwt, SECRET_KEY)).isEqualTo(EAMIL);
	}

	@Test
	void throw_exception_when_token_has_expired() throws Exception {
		String jwt = JwtTokenUtils.generateToken(EAMIL, SECRET_KEY, 0L);
		Thread.sleep(100L);
		assertThatThrownBy(() -> JwtTokenUtils.getEmail(jwt, SECRET_KEY))
			.isInstanceOf(ExpiredJwtException.class);
	}

	@Test
	void throw_exception_when_try_decrypt_with_wrong_key() throws Exception {
		String jwt = JwtTokenUtils.generateToken(EAMIL, SECRET_KEY, 0L);
		Thread.sleep(100L);
		assertThatThrownBy(() -> JwtTokenUtils.getEmail(jwt, "test-wrong-key-okr-project-jwt-token"))
			.isInstanceOf(SignatureException.class);
	}
}