package kr.jay.okrver3.interfaces.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import kr.jay.okrver3.application.user.LoginInfo;
import kr.jay.okrver3.application.user.UserFacade;
import kr.jay.okrver3.domain.user.ProviderType;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

	@Mock
	private UserFacade userFacade;

	@Mock
	private TokenVerifier tokenVerifier;
	private UserApiController sut;


	@BeforeEach
	void setUp() throws Exception {
		sut = new UserApiController(tokenVerifier,userFacade);

	}
//
	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {
		given(tokenVerifier.verifyIdToken("idToken")).willReturn(new OAuth2UserInfo("googleId", "userName", "email","pictureUrl",ProviderType.GOOGLE));
		given(userFacade.getLoginInfoFrom(new OAuth2UserInfo("googleId", "userName", "email","pictureUrl",ProviderType.GOOGLE)))
			.willReturn(Optional.empty());
		given(userFacade.createGuestInfoFrom(new OAuth2UserInfo("googleId", "userName", "email","pictureUrl",ProviderType.GOOGLE)))
			.willReturn(new LoginInfo("guest-12301", "name", "email", ProviderType.GOOGLE, "pic",null, null));


		final ResponseEntity<LoginResponse> response = sut.loginWithIdToken("GOOGLE", "idToken");

		assertThat(response.getBody()).isEqualTo(
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

	@Test
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {

		OAuth2UserInfo info = new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl", ProviderType.GOOGLE);

		given(tokenVerifier.verifyIdToken("idToken"))
			.willReturn(info);
		given(userFacade.getLoginInfoFrom(info))
			.willThrow(new IllegalArgumentException(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다."));

		assertThatThrownBy(() -> sut.loginWithIdToken("GOOGLE", "idToken"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");
	}

}