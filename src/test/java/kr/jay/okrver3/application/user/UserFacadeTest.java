package kr.jay.okrver3.application.user;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

class UserFacadeTest {

	private UserFacade sut;

	@BeforeEach
	void setUp() throws Exception {
		sut = new UserFacade();
	}

	@Test
	@DisplayName("가입한 유저 정보가 없는 OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(
			new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl", ProviderType.GOOGLE));

		Assertions.assertThat(loginInfo).isEqualTo(
			new LoginInfo(
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