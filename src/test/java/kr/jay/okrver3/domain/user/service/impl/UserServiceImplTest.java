package kr.jay.okrver3.domain.user.service.impl;

import static kr.jay.okrver3.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.info.UserInfo;
import kr.jay.okrver3.infrastructure.token.RefreshTokenRepositoryImpl;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;

@DataJpaTest
@Import({UserServiceImpl.class, RefreshTokenRepositoryImpl.class})
class UserServiceImplTest {

	@Autowired
	private UserServiceImpl sut;

	@Test
	@Sql("classpath:insert-different-social-google-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info = DiffAppleUserInfoFixture.build();

		assertThatThrownBy(() -> sut.getUserInfoFrom(info))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage("소셜 provider 불일치, " + ProviderType.GOOGLE.getName() + "(으)로 가입한 계정이 있습니다.");
	}

	@Test
	@DisplayName("가입한 유저 정보가 없을때 idToken을 통해 로그인을 시도하면 기대하는 응답(Optional.empty())을 반환한다.")
	void try_to_login_with_social_IdToken_for_the_first_time() throws Exception {

		OAuth2UserInfo info = AppleUserInfoFixture.build();

		Optional<UserInfo> userInfoFrom = sut.getUserInfoFrom(info);

		assertThat(userInfoFrom).isEmpty();
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있을때 idToken을 통해 로그인을 시도하면 기대하는 응답(UserInfo)을 반환한다.")
	void will_return_userInfo_when_user_try_to_login_with_idToken() throws Exception {

		OAuth2UserInfo info = AppleUserInfoFixture.build();

		Optional<UserInfo> userInfoFrom = sut.getUserInfoFrom(info);

		assertThat(userInfoFrom).isNotEmpty();
		assertThat(userInfoFrom.get().email()).isEqualTo(info.email());
		assertThat(userInfoFrom.get().id()).isEqualTo(info.id());
		assertThat(userInfoFrom.get().name()).isEqualTo(info.name());
		assertThat(userInfoFrom.get().profileImageUrl()).isEqualTo(info.picture());
		assertThat(userInfoFrom.get().providerType()).isEqualTo(info.providerType());
	}

	@Test
	@DisplayName("가입한 유저 정보가 없을때 회원가입을 시도하면 기대하는 응답(UserInfo)을 반환한다.")
	void will_return_userInfo_when_guest_try_to_join() throws Exception {

		String newNameFromGuest = "newNameFromGuest";
		String guestEmail = "apple@apple.com";
		GuestInfo guestInfo = new GuestInfo("guest-rkmZUIUNWkSMX3", "gusetId", guestEmail, "guest",
			ProviderType.GOOGLE, "pic");
		JoinRequest joinRequest = new JoinRequest("guest-rkmZUIUNWkSMX3", newNameFromGuest,
			guestEmail, "WEB_SERVER_DEVELOPER");

		UserInfo userInfo = sut.registerNewUserFrom(guestInfo, joinRequest);

		assertThat(userInfo.email()).isEqualTo(guestEmail);
		assertThat(userInfo.id()).isNotNull();
		assertThat(userInfo.name()).isEqualTo(newNameFromGuest);
		assertThat(userInfo.profileImageUrl()).isEqualTo("pic");
		assertThat(userInfo.providerType()).isEqualTo(ProviderType.GOOGLE);

	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("이미 가입한 유저 정보가 있을때 회원가입을 시도하면 기대하는 응답(exception)을 반환한다.")
	void will_throw_exception_when_member_request_to_join() throws Exception {

		GuestInfo guestInfo = new GuestInfo("already-joined-guest", "gusetId", "apple@apple.com", "alreadyJoinedUser",
			ProviderType.GOOGLE, "pic");
		JoinRequest joinRequest = new JoinRequest("already-joined-guest", "alreadyJoinedUser",
			"apple@apple.com", "dev");

		assertThatThrownBy(() -> sut.registerNewUserFrom(guestInfo, joinRequest))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

}