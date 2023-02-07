package kr.jay.okrver3.interfaces.user;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.FakeTokenVerifier;
import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.domain.user.ProviderType;

@Import(TestConfig.class)
@SpringBootTest
class UserApiControllerTest {


	@Autowired
	private UserApiController sut;

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		final ResponseEntity<LoginResponse> response = sut.loginWithIdToken("GOOGLE", "idToken");

		assertLoginResponse(response.getBody());
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {

		assertThatThrownBy(() -> sut.loginWithIdToken("GOOGLE", "idToken"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");
	}

	private static void assertLoginResponse(LoginResponse body) {
		assertThat(body.guestId()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(body.name()).isEqualTo(FakeTokenVerifier.name);
		assertThat(body.email()).isEqualTo(FakeTokenVerifier.email);
		assertThat(body.providerType()).isEqualTo(FakeTokenVerifier.provider);
		assertThat(body.accessToken()).isNull();
		assertThat(body.refreshToken()).isNull();
	}
}