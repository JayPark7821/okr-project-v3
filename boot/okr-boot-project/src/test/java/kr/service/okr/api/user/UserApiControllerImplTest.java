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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.AuthenticationInfo;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.api.JobResponse;
import kr.service.okr.user.api.JoinRequest;
import kr.service.okr.user.api.LoginResponse;
import kr.service.okr.user.api.TokenResponse;
import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.okr.user.persistence.entity.user.UserJpaEntity;
import kr.service.okr.utils.SpringBootTestReady;

@Transactional
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

	@Test
	@DisplayName("게스트 정보가 없을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_before_guest_login() {
		JoinRequest joinRequestDto = new JoinRequest("not-registered-guest-id", "guest", "notMember@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequestDto))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() {

		JoinRequest joinRequestDto = new JoinRequest("guest-rkmZUIUNWkSMX3", "guest", "teamMemberTest@naver.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequestDto))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	@Test
	void getJobCategory를_호출하면_기대하는_응답_JobResponse를_반환한다() throws Exception {
		ResponseEntity<List<JobResponse>> response = sut.getJobCategory();
		assertThat(response.getBody().size()).isEqualTo(6);
	}

	@Test
	void getJobField를_호출하면_기대하는_응답_JobResponse를_반환한다() throws Exception {
		String category = "BACK_END";
		ResponseEntity<List<JobResponse>> response = sut.getJobField(category);
		assertThat(response.getBody().size()).isEqualTo(4);
	}

	@Test
	@DisplayName("AccessToken 만료시 RefreshToken으로 새로운 AccessToken을 요청하면 기대하는 응답을 반환한다.")
	void request_new_accecssToken_with_refreshToken() throws Exception {

		final RefreshToken refreshToken = RefreshToken.generateNewRefreshToken("teamMemberTest@naver.com");
		em.persist(new RefreshTokenJpaEntity((refreshToken)));

		ResponseEntity<TokenResponse> response = sut.getNewAccessToken(getAuthenticationInfo(111L));

		assertThat(response.getBody().accessToken()).isNotNull();
		assertThat(response.getBody().refreshToken()).isEqualTo(refreshToken.getRefreshToken());
	}

	private void assertUserJoinResponse(final ResponseEntity<LoginResponse> response) {
		assertThat(response.getBody().guestUserId()).isNull();
		assertThat(response.getBody().name()).isEqualTo("guest");
		assertThat(response.getBody().email()).isEqualTo("guest@email");
		assertThat(response.getBody().providerType()).isEqualTo(ProviderType.GOOGLE.name());
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

	private AuthenticationInfo getAuthenticationInfo(Long userSeq) {
		final UserJpaEntity user = em.createQuery("select u from UserJpaEntity u where u.userSeq = :userSeq",
				UserJpaEntity.class)
			.setParameter("userSeq", userSeq)
			.getSingleResult();
		return new AuthenticationInfo(user.getUserSeq(), user.getEmail(), user.getUsername());
	}

}
