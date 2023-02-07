package kr.jay.okrver3.domain.user.service.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.service.UserReader;
import kr.jay.okrver3.infrastructure.user.UserReaderImpl;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

@DataJpaTest
@Import({UserServiceImpl.class, UserReaderImpl.class})
class UserServiceImplTest{

	@Autowired
	private UserServiceImpl sut;

	@Autowired
	private UserReader userReader;

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
}