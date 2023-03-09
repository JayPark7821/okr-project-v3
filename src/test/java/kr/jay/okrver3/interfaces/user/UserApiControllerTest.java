package kr.jay.okrver3.interfaces.user;

import static kr.jay.okrver3.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.response.JobResponse;
import kr.jay.okrver3.interfaces.user.response.LoginResponse;
import kr.jay.okrver3.interfaces.user.response.TokenResponse;

@Import(TestConfig.class)
@Transactional
@SpringBootTest
class UserApiControllerTest {

	@Autowired
	private UserApiController sut;

	@Value("${app.auth.tokenSecret}")
	private String key;

	@PersistenceContext
	EntityManager em;


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
			.hasMessage("소셜 provider 불일치, " + ProviderType.GOOGLE.getName() + "(으)로 가입한 계정이 있습니다.");
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

	@Test
	@Sql("classpath:insert-user.sql")
	void refreshToken으로_getNewAccessToken을_호출하면_기대하는_응답을_리턴한다_new_accessToken() {

		String accessToken = JwtTokenUtils.generateToken("apple@apple.com", key, 10000000000000L);
		em.persist(new RefreshToken("apple@apple.com",accessToken ));

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + accessToken);

		ResponseEntity<TokenResponse> response = sut.getNewAccessToken(request);

		assertThat(response.getBody().accessToken()).isNotNull();
		assertThat(response.getBody().refreshToken()).isEqualTo(accessToken);
	}



	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("프로젝트 생성시 팀원을 추가하기 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address_for_register_project() throws Exception {
		String memberEmail = "guest@email.com";

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 999L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<String> response = sut.validateEmail(memberEmail, auth);
		assertThat(response.getBody()).isEqualTo(memberEmail);
	}


	@Test
	void getJobCategory를_호출하면_기대하는_응답_JobCategoryResponse를_반환한다() throws Exception {

		ResponseEntity<List<JobResponse>> response = sut.getJobCategory();
		assertThat(response.getBody()).isEqualTo(6);
	}


	private static void assertGuestLoginResponse(LoginResponse body) {
		assertThat(body.guestId()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(body.name()).isEqualTo(DiffAppleUserInfoFixture.NAME);
		assertThat(body.email()).isEqualTo(DiffAppleUserInfoFixture.EMAIL);
		assertThat(body.providerType()).isEqualTo(DiffAppleUserInfoFixture.PROVIDER_TYPE);
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