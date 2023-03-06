package kr.jay.okrver3.interfaces.user;

import static kr.jay.okrver3.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.response.LoginResponse;

@Import(TestConfig.class)
@Transactional
@SpringBootTest
class UserApiControllerTest {

	@Autowired
	private UserApiController sut;

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		final ResponseEntity<LoginResponse> response = sut.loginWithIdToken("GOOGLE", "googleToken");

		assertGuestLoginResponse(response.getBody());
	}

	@Test
	@Sql("classpath:insert-different-social-google-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {

		assertThatThrownBy(() -> sut.loginWithIdToken("GOOGLE", "googleToken"))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage("소셜 provider 불일치, " + ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있을때 가입한 소셜idToken을 통해 로그인을 시도하면 기대하는 응답(LoginResponse)을 반환한다.")
	void login_With_social_IdToken() throws Exception {

		ResponseEntity<LoginResponse> response = sut.loginWithIdToken("APPLE", "appleToken");

		assertUserLoginResponse(response.getBody());
	}

	@Test
	@Sql("classpath:insert-guest.sql")
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() {
		JoinRequest joinRequestDto = new JoinRequest("guest-rkmZUIUNWkSMX3", "guest", "guest@email.com",
			"WEB_FRONT_END_DEVELOPER");

		ResponseEntity<LoginResponse> response = sut.join(joinRequestDto);

		assertThat(response.getBody().guestId()).isNull();
		assertThat(response.getBody().name()).isEqualTo("guest");
		assertThat(response.getBody().email()).isEqualTo("guest@email.com");
		assertThat(response.getBody().providerType()).isEqualTo(ProviderType.GOOGLE);
		assertThat(response.getBody().accessToken()).isNotNull();
		assertThat(response.getBody().refreshToken()).isNotNull();

	}

	@Test
	@DisplayName("게스트 정보가 없을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_before_guest_login() {
		JoinRequest joinRequestDto = new JoinRequest("not-registered-guest-id", "guest", "guest@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequestDto))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-guest.sql"})
	@DisplayName("가입한 유저 정보가 있을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() {

		JoinRequest joinRequestDto = new JoinRequest("guest-rkmZUIUNWkSMX3", "guest", "guest@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequestDto))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	private static void assertGuestLoginResponse(LoginResponse body) {
		assertThat(body.guestId()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(body.name()).isEqualTo(GoogleUserInfoFixture.NAME);
		assertThat(body.email()).isEqualTo(GoogleUserInfoFixture.EMAIL);
		assertThat(body.providerType()).isEqualTo(GoogleUserInfoFixture.PROVIDER_TYPE);
		assertThat(body.accessToken()).isNull();
		assertThat(body.refreshToken()).isNull();
	}

	private static void assertUserLoginResponse(LoginResponse body) {
		assertThat(body.guestId()).isNull();
		assertThat(body.name()).isEqualTo(AppleUserInfoFixture.NAME);
		assertThat(body.email()).isEqualTo(AppleUserInfoFixture.EMAIL);
		assertThat(body.providerType()).isEqualTo(AppleUserInfoFixture.PROVIDER_TYPE);
		assertThat(body.accessToken()).isNotNull();
		assertThat(body.refreshToken()).isNotNull();
	}
}