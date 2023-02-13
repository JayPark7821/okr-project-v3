package kr.jay.okrver3.domain.token.service.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kr.jay.okrver3.domain.token.service.AuthTokenInfo;

@DataJpaTest
@Import(TokenServiceImpl.class)
class TokenServiceImplTest {

	@Autowired
	private TokenServiceImpl sut;

	@Test
	@DisplayName("email이 입력 되었을때 기대하는 응답(AuthTokenInfo)을 반환한다.")
	void create_new_guest_from_oauth2info () throws Exception {
		String email = "google@gmail.com";

		AuthTokenInfo authTokenInfo = sut.generateTokenSet(email);

		assertThat(authTokenInfo.accessToken()).isNotNull();
		assertThat(authTokenInfo.refreshToken()).isNotNull();
	}

}