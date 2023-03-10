package kr.jay.okrver3.application.user;

import static kr.jay.okrver3.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.guset.service.impl.GuestServiceImpl;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.impl.TokenServiceImpl;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.UserInfoUpdateCommand;
import kr.jay.okrver3.domain.user.info.JobInfo;
import kr.jay.okrver3.domain.user.info.LoginInfo;
import kr.jay.okrver3.domain.user.service.impl.UserServiceImpl;
import kr.jay.okrver3.infrastructure.guest.GuestReaderImpl;
import kr.jay.okrver3.infrastructure.guest.GuestStoreImpl;
import kr.jay.okrver3.infrastructure.token.RefreshTokenRepositoryImpl;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.request.UserInfoUpdateRequest;

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
	@DisplayName("????????? ?????? ????????? ?????? OAuth2UserInfo??? ???????????? ??? ???????????? ??????(Optional.empty())??? ????????????.")
	void not_joined_user_will_return_optional_empty() throws Exception {

		OAuth2UserInfo info = DiffAppleUserInfoFixture.build();

		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(info);

		assertThat(loginInfo).isEmpty();
	}

	@Test
	@Sql("classpath:insert-different-social-google-user.sql")
	@DisplayName("????????? ?????? ????????? ????????? ????????? ?????? ????????? ?????? ?????? idToken??? ?????? ???????????? ???????????? ???????????? ??????(Exception)??? ????????????.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info = DiffAppleUserInfoFixture.build();

		assertThatThrownBy(() -> sut.getLoginInfoFrom(info))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage("?????? provider ?????????, " + ProviderType.GOOGLE.getName() + "(???)??? ????????? ????????? ????????????.");

	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("????????? ?????? ????????? ?????? OAuth2UserInfo??? ???????????? ??? ???????????? ??????(Optional.of(LoginInfo)??? ????????????.")
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
	@DisplayName("OAuth2UserInfo??? ???????????? ??? ???????????? ??????(Guest)??? ????????????.")
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
	@DisplayName("????????? ????????? ?????? ??? join()??? ???????????? ???????????? ????????? ????????????.")
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
	@DisplayName("????????? ????????? ?????? ??? join()??? ???????????? ???????????? ????????? ?????????.")
	void join_before_guest_login() {

		JoinRequest joinRequest = new JoinRequest("not-registered-guest-id", "guest", "guest@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequest))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-guest.sql"})
	@DisplayName("????????? ?????? ????????? ?????? ??? join()??? ???????????? ???????????? ????????? ?????????.")
	void join_again_when_after_join() {

		JoinRequest joinRequest = new JoinRequest("guest-rkmZUIUNWkSMX3", "guest", "guest@email.com",
			"Developer");

		assertThatThrownBy(() -> sut.join(joinRequest))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.ALREADY_JOINED_USER.getMessage());

	}

	@Test
	@Sql("classpath:insert-user.sql")
	void refreshToken??????_getNewAccessToken???_????????????_????????????_?????????_????????????_new_accessToken() {

		String accessToken = JwtTokenUtils.generateToken("apple@apple.com", key, 10000000000000L);
		em.persist(new RefreshToken("apple@apple.com",accessToken ));

		AuthTokenInfo info = sut.getNewAccessToken(accessToken);

		assertThat(info.accessToken()).isNotNull();
		assertThat(info.refreshToken()).isEqualTo(accessToken);

	}


	@Test
	@Sql({"classpath:insert-user.sql"})
	@DisplayName("???????????? ????????? ????????? ???????????? ?????? email??? ???????????? ???????????? ??????(email)??? ????????????.")
	void validate_email_address_for_register_project() throws Exception {
		String memberEmail = "guest@email.com";

		User user = getUser(999L);

		final String response = sut.validateEmail(memberEmail, user.getUserSeq());

		assertThat(response).isEqualTo(memberEmail);
	}

	@Test
	void getJobCategory???_????????????_????????????_??????_JobResponse???_????????????() throws Exception {

		List<JobInfo> response = sut.getJobCategory();
		assertThat(response.size()).isEqualTo(6);
	}


	@Test
	void getJobField???_????????????_????????????_??????_JobResponse???_????????????() throws Exception {

		JobCategory category = JobCategory.BACK_END;
		List<JobInfo> response = sut.getJobField(category);
		assertThat(response.size()).isEqualTo(4);
	}

	@Test
	@Sql("classpath:insert-user.sql")
	void updateUserInfo???_????????????_????????????_?????????_????????????() throws Exception {
		String newUserName = "newName";
		String newJobField = "LAW_LABOR";

		sut.updateUserInfo(new UserInfoUpdateCommand(newUserName,"profileImage",
			newJobField), 999L);

		User updatedUser = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 999L)
			.getSingleResult();

		assertThat(updatedUser.getUsername()).isEqualTo(newUserName);
		assertThat(updatedUser.getJobField().getCode()).isEqualTo(newJobField);
	}

	private User getUser(Long seq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", seq)
			.getSingleResult();
		return user;
	}
}