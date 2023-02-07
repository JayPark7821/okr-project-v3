package kr.jay.okrver3.domain.user.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserReader;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest{

	private UserServiceImpl sut;

	@Mock
	private UserReader userReader;

	@BeforeEach
	void setUp(){
		sut = new UserServiceImpl(userReader);
	}

	@Test
	@DisplayName("가입한 유저 정보가 있지만 가입한 소셜 정보와 다른 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Exception)을 반환한다.")
	void login_With_different_social_IdToken() throws Exception {
		OAuth2UserInfo info =
			new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl", ProviderType.GOOGLE);

		User user = User.builder()
			.userSeq(1L)
			.email("email")
			.userId("appleId")
			.profileImage("pictureUrl")
			.providerType(ProviderType.APPLE)
			.build();

		given(userReader.findByEmail(info.email()))
			.willReturn(Optional.of(user));

		assertThatThrownBy(() -> sut.getUserInfoFrom(info))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(ProviderType.APPLE.getName() + "(으)로 가입한 계정이 있습니다.");

	}
}