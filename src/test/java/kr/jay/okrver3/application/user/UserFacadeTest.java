package kr.jay.okrver3.application.user;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.guset.service.impl.GuestServiceImpl;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.service.impl.UserServiceImpl;
import kr.jay.okrver3.infrastructure.user.UserReaderImpl;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

@DataJpaTest
@Import({UserFacade.class, UserServiceImpl.class, UserReaderImpl.class, GuestServiceImpl.class })
class UserFacadeTest {

	@Autowired
	private UserFacade sut;

	@Test
	@DisplayName("가입한 유저 정보가 없는 OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Optional.empty())을 반환한다.")
	void not_joined_user_will_return_optional_empty() throws Exception {

		OAuth2UserInfo info = new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl",
			ProviderType.GOOGLE);


		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(info);

		assertThat(loginInfo).isEmpty();
	}

	@Test
	@DisplayName("OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Guest)을 반환한다.")
	void when_OAuth2UserInfo_were_given_will_return_guest() throws Exception {

		OAuth2UserInfo info = new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl",
			ProviderType.GOOGLE);

		LoginInfo guestInfo = sut.createGuestInfoFrom(info);

		assertThat(guestInfo.guestUuid()).isEqualTo("guestUuid");
		assertThat(guestInfo.email()).isEqualTo("email");
		assertThat(guestInfo.name()).isEqualTo("userName");
		assertThat(guestInfo.profileImageUrl()).isEqualTo("pictureUrl");
		assertThat(guestInfo.providerType()).isEqualTo(ProviderType.GOOGLE);
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info =
			new OAuth2UserInfo("googleId", "userName", "apple@apple.com", "pictureUrl", ProviderType.GOOGLE);

		assertThatThrownBy(() -> sut.getLoginInfoFrom(info))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");

	}
}