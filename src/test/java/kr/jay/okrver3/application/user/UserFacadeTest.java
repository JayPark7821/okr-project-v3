package kr.jay.okrver3.application.user;

import static kr.jay.okrver3.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.guset.service.impl.GuestServiceImpl;
import kr.jay.okrver3.domain.token.service.impl.TokenServiceImpl;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.service.impl.UserServiceImpl;
import kr.jay.okrver3.infrastructure.guest.GuestReaderImpl;
import kr.jay.okrver3.infrastructure.guest.GuestStoreImpl;
import kr.jay.okrver3.infrastructure.user.UserReaderImpl;
import kr.jay.okrver3.interfaces.user.auth.OAuth2UserInfo;

@DataJpaTest
@Import({UserFacade.class, UserServiceImpl.class, UserReaderImpl.class, GuestServiceImpl.class, GuestStoreImpl.class,
	GuestReaderImpl.class, TokenServiceImpl.class})
class UserFacadeTest {

	@Autowired
	private UserFacade sut;

	@Test
	@DisplayName("가입한 유저 정보가 없는 OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Optional.empty())을 반환한다.")
	void not_joined_user_will_return_optional_empty() throws Exception {

		OAuth2UserInfo info = GoogleUserInfoFixture.build();

		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(info);

		assertThat(loginInfo).isEmpty();
	}

	@Test
	@Sql("classpath:insert-different-social-google-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info = GoogleUserInfoFixture.build();

		assertThatThrownBy(() -> sut.getLoginInfoFrom(info))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");

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

		OAuth2UserInfo info = GoogleUserInfoFixture.build();

		LoginInfo guestInfo = sut.createGuestInfoFrom(info);

		assertThat(guestInfo.guestUuid()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(guestInfo.email()).isEqualTo(GoogleUserInfoFixture.EMAIL);
		assertThat(guestInfo.name()).isEqualTo(GoogleUserInfoFixture.NAME);
		assertThat(guestInfo.profileImageUrl()).isEqualTo(GoogleUserInfoFixture.PIC);
		assertThat(guestInfo.providerType()).isEqualTo(GoogleUserInfoFixture.PROVIDER_TYPE);
	}

}