package kr.service.user.api.user;

import static kr.service.user.utils.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.api.LoginResponse;
import kr.service.user.api.user.external.UserExternalApiControllerImpl;
import kr.service.user.utils.TestConfig;

@Import(TestConfig.class)
@Transactional
@SpringBootTest
class UserExternalApiControllerImplTest {

	@Autowired
	private UserExternalApiControllerImpl sut;

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		final ResponseEntity<LoginResponse> response = sut.loginWithIdToken("GOOGLE", "googleToken");

		assertGuestLoginResponse(response.getBody());
	}

	private static void assertGuestLoginResponse(LoginResponse body) {
		assertThat(body.guestUserId()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(body.name()).isEqualTo(DiffAppleUserInfoFixture.NAME);
		assertThat(body.email()).isEqualTo(DiffAppleUserInfoFixture.EMAIL);
		assertThat(body.providerType()).isEqualTo(DiffAppleUserInfoFixture.PROVIDER_TYPE);
		assertThat(body.accessToken()).isNull();
		assertThat(body.refreshToken()).isNull();
	}

}