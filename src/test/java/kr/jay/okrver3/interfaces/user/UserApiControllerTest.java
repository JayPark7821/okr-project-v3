package kr.jay.okrver3.interfaces.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.jay.okrver3.domain.user.ProviderType;

class UserApiControllerTest {


	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		final UserApiController sut = new UserApiController();
		final LoginResponse response = sut.loginWithIdToken("GOOGLE", "idToken");

		Assertions.assertThat(response).isEqualTo(
			new LoginResponse(
				"guest-12301",
				"name",
				"email",
				ProviderType.GOOGLE,
				null,
				null
			)
		);
	}
}