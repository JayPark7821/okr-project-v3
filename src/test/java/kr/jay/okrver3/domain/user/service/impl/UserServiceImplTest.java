package kr.jay.okrver3.domain.user.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.service.UserInfo;
import kr.jay.okrver3.infrastructure.user.UserReaderImpl;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

@DataJpaTest
@Import({UserServiceImpl.class, UserReaderImpl.class})
class UserServiceImplTest{

	@Autowired
	private UserServiceImpl sut;


	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info =
			new OAuth2UserInfo("googleId", "userName", "apple@apple.com", "pictureUrl", ProviderType.GOOGLE);

		assertThatThrownBy(() -> sut.getUserInfoFrom(info))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");
	}

	@Test
	@DisplayName("가입한 유저 정보가 없을때 idToken을 통해 로그인을 시도하면 기대하는 응답(Optional.empty())을 반환한다.")
	void try_to_login_with_social_IdToken_for_the_first_time() throws Exception {

		OAuth2UserInfo info =
			new OAuth2UserInfo("googleId", "userName", "apple@apple.com", "pictureUrl", ProviderType.GOOGLE);

		Optional<UserInfo> userInfoFrom = sut.getUserInfoFrom(info);

		assertThat(userInfoFrom).isEmpty();
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있을때 idToken을 통해 로그인을 시도하면 기대하는 응답(UserInfo)을 반환한다.")
	void will_return_userInfo_when_user_try_to_login_with_idToken() throws Exception {
		OAuth2UserInfo info =
			new OAuth2UserInfo("appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE);

		Optional<UserInfo> userInfoFrom = sut.getUserInfoFrom(info);

		assertThat(userInfoFrom).isNotEmpty();
		assertThat(userInfoFrom.get().email()).isEqualTo(info.email());
		assertThat(userInfoFrom.get().id()).isEqualTo(info.id());
		assertThat(userInfoFrom.get().name()).isEqualTo(info.name());
		assertThat(userInfoFrom.get().profileImageUrl()).isEqualTo(info.picture());
		assertThat(userInfoFrom.get().providerType()).isEqualTo(info.providerType());
	}
}