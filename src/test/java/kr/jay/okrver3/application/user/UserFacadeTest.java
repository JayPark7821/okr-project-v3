package kr.jay.okrver3.application.user;

import static kr.jay.okrver3.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.guset.service.impl.GuestServiceImpl;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.impl.TokenServiceImpl;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.LoginInfo;
import kr.jay.okrver3.domain.user.service.impl.UserServiceImpl;
import kr.jay.okrver3.infrastructure.guest.GuestReaderImpl;
import kr.jay.okrver3.infrastructure.guest.GuestStoreImpl;
import kr.jay.okrver3.infrastructure.token.RefreshTokenRepositoryImpl;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;

@DataJpaTest
@Import({UserFacade.class, UserServiceImpl.class, GuestServiceImpl.class, GuestStoreImpl.class,
	GuestReaderImpl.class, TokenServiceImpl.class, RefreshTokenRepositoryImpl.class})
class UserFacadeTest {

	@PersistenceContext
	EntityManager em;

	@Value("${app.auth.tokenSecret}")
	private String key;
	@Autowired
	private UserFacade sut;

	@Test
	@DisplayName("가입한 유저 정보가 없는 OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Optional.empty())을 반환한다.")
	void not_joined_user_will_return_optional_empty() throws Exception {

		OAuth2UserInfo info = DiffAppleUserInfoFixture.build();

		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(info);

		assertThat(loginInfo).isEmpty();
	}

	@Test
	@Sql("classpath:insert-different-social-google-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info = DiffAppleUserInfoFixture.build();

		assertThatThrownBy(() -> sut.getLoginInfoFrom(info))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage("소셜 provider 불일치, " + ProviderType.GOOGLE.getName() + "(으)로 가입한 계정이 있습니다.");

	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있는 OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Optional.of(LoginInfo)을 반환한다.")
	void joined_user_will_return_optional_loginInfo() throws Exception {

		OAuth2UserInfo info = AppleUserInfoFixture.build();

		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(info);

		assertThat(loginInfo.get().name()).isEqualTo(AppleUserInfoFixture.NAME);
		assertThat(loginInfo.get().email()).isEqualTo(AppleUserInfoFixture.EMAIL);
		assertThat(loginInfo.get().guestUuid()).isNull();
		assertThat(loginInfo.get().profileImageUrl()).isEqualTo(AppleUserInfoFixture.PIC);
		assertThat(loginInfo.get().providerType()).isEqualTo(AppleUserInfoFixture.PROVIDER_TYPE);
		assertThat(loginInfo.get().accessToken()).isNotNull();
		assertThat(loginInfo.get().refreshToken()).isNotNull();
	}

	@Test
	@DisplayName("OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Guest)을 반환한다.")
	void when_OAuth2UserInfo_were_given_will_return_guest() throws Exception {

		OAuth2UserInfo info = DiffAppleUserInfoFixture.build();

		LoginInfo guestInfo = sut.createGuestInfoFrom(info);

		assertThat(guestInfo.guestUuid()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(guestInfo.email()).isEqualTo(DiffAppleUserInfoFixture.EMAIL);
		assertThat(guestInfo.name()).isEqualTo(DiffAppleUserInfoFixture.NAME);
		assertThat(guestInfo.profileImageUrl()).isEqualTo(DiffAppleUserInfoFixture.PIC);
		assertThat(guestInfo.providerType()).isEqualTo(DiffAppleUserInfoFixture.PROVIDER_TYPE);
	}

	@Test
	@Sql("classpath:insert-guest.sql")
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() {

		String guestNameFromUser = "newGuestName";
		String registeredGuestEmail = "guest@email.com";
		JoinRequest joinRequest = new JoinRequest("guest-rkmZUIUNWkSMX3", guestNameFromUser,
			registeredGuestEmail,
			"WEB_SERVER_DEVELOPER");

		LoginInfo loginInfo = sut.join(joinRequest);

		assertThat(loginInfo.name()).isEqualTo(guestNameFromUser);
		assertThat(loginInfo.email()).isEqualTo(registeredGuestEmail);
		assertThat(loginInfo.guestUuid()).isNull();
		assertThat(loginInfo.profileImageUrl()).isEqualTo("pic");
		assertThat(loginInfo.providerType()).isEqualTo(ProviderType.GOOGLE);
		assertThat(loginInfo.accessToken()).isNotNull();
		assertThat(loginInfo.refreshToken()).isNotNull();

	}

	@Test
	@DisplayName("게스트 정보가 없을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_before_guest_login() {

		JoinRequest joinRequest = new JoinRequest("not-registered-guest-id", "guest", "guest@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequest))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-guest.sql"})
	@DisplayName("가입한 유저 정보가 있을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() {

		JoinRequest joinRequest = new JoinRequest("guest-rkmZUIUNWkSMX3", "guest", "guest@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequest))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.ALREADY_JOINED_USER.getMessage());

	}

	@Test
	@Sql("classpath:insert-user.sql")
	void refreshToken으로_getNewAccessToken을_호출하면_기대하는_응답을_리턴한다_new_accessToken() {

		String accessToken = JwtTokenUtils.generateToken("apple@apple.com", key, 10000000000000L);
		em.persist(new RefreshToken("apple@apple.com",accessToken ));

		AuthTokenInfo info = sut.getNewAccessToken(accessToken);

		assertThat(info.accessToken()).isNotNull();
		assertThat(info.refreshToken()).isEqualTo(accessToken);

	}


	@Test
	@Sql({"classpath:insert-user.sql"})
	@DisplayName("프로젝트 생성시 팀원을 추가하기 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address_for_register_project() throws Exception {
		String memberEmail = "guest@email.com";

		User user = getUser(999L);

		final String response = sut.validateEmail(memberEmail, user.getUserSeq());

		assertThat(response).isEqualTo(memberEmail);
	}

	private User getUser(Long seq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", seq)
			.getSingleResult();
		return user;
	}
}