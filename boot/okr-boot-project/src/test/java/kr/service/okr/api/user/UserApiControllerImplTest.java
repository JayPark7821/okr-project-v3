package kr.service.okr.api.user;

import static kr.service.okr.utils.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.api.JoinRequest;
import kr.service.okr.user.api.LoginResponse;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.utils.SpringBootTestReady;

public class UserApiControllerImplTest extends SpringBootTestReady {

	@Autowired
	private UserApiControllerImpl sut;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
	}

	@PersistenceContext
	EntityManager em;

	@Test
	@DisplayName("가입한 유저 정보가 없을 때 idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		final ResponseEntity<LoginResponse> response = sut.loginWithIdToken("APPLE", "notMemberAppleIdToken");

		assertGuestLoginResponse(response.getBody());
	}

	@Test
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {

		assertThatThrownBy(() -> sut.loginWithIdToken("GOOGLE", "googleToken"))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.MISS_MATCH_PROVIDER.getMessage().formatted(ProviderType.GOOGLE.name()));
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을때 가입한 소셜idToken을 통해 로그인을 시도하면 기대하는 응답(LoginResponse)을 반환한다.")
	void login_With_social_IdToken() throws Exception {

		ResponseEntity<LoginResponse> response = sut.loginWithIdToken("APPLE", "memberAppleIdToken");

		assertUserLoginResponse(response.getBody());
	}

	@Test
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() {
		JoinRequest joinRequestDto = new JoinRequest("guest-ttdxe", "guest", "guest@email",
			"WEB_FRONT_END_DEVELOPER");

		ResponseEntity<LoginResponse> response = sut.join(joinRequestDto);

		assertUserJoinResponse(response);

	}

	private void assertUserJoinResponse(final ResponseEntity<LoginResponse> response) {
		assertThat(response.getBody().guestUserId()).isNull();
		assertThat(response.getBody().name()).isEqualTo("guest");
		assertThat(response.getBody().email()).isEqualTo("guest@email");
		assertThat(response.getBody().providerType()).isEqualTo(ProviderType.GOOGLE);
		assertThat(response.getBody().accessToken()).isNotNull();
		assertThat(response.getBody().refreshToken()).isNotNull();
	}

	private void assertGuestLoginResponse(LoginResponse body) {
		assertThat(body.guestUserId()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(body.name()).isEqualTo(NotMemberAppleInfoFixture.NAME);
		assertThat(body.email()).isEqualTo(NotMemberAppleInfoFixture.EMAIL);
		assertThat(body.providerType()).isEqualTo(NotMemberAppleInfoFixture.PROVIDER_TYPE.name());
		assertThat(body.accessToken()).isNull();
		assertThat(body.refreshToken()).isNull();
	}

	private void assertUserLoginResponse(LoginResponse body) {
		assertThat(body.guestUserId()).isNull();
		assertThat(body.name()).isEqualTo(MemberAppleInfoFixture.NAME);
		assertThat(body.email()).isEqualTo(MemberAppleInfoFixture.EMAIL);
		assertThat(body.providerType()).isEqualTo(MemberAppleInfoFixture.PROVIDER_TYPE.name());
		assertThat(body.accessToken()).isNotNull();
		assertThat(body.refreshToken()).isNotNull();
	}
}
