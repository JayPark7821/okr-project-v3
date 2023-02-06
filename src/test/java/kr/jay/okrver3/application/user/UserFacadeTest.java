package kr.jay.okrver3.application.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

	private UserFacade sut;

	@Mock
	private UserService userService;

	@Mock
	private GuestService guestService;

	@BeforeEach
	void setUp() throws Exception {
		sut = new UserFacade(userService, guestService);
	}

	@Test
	@DisplayName("가입한 유저 정보가 없는 OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Optional.empty())을 반환한다.")
	void not_joined_user_will_return_optional_empty() throws Exception {

		OAuth2UserInfo info = new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl",
			ProviderType.GOOGLE);

		given(userService.getUserInfoFrom(info))
			.willReturn(Optional.empty());

		Optional<LoginInfo> loginInfo = sut.getLoginInfoFrom(info);

		assertThat(loginInfo).isEmpty();
	}

	@Test
	@DisplayName("OAuth2UserInfo가 넘어왔을 때 기대하는 응답(Guest)을 반환한다.")
	void when_OAuth2UserInfo_were_given_will_return_guest() throws Exception {

		OAuth2UserInfo info = new OAuth2UserInfo("googleId", "userName", "email", "pictureUrl",
			ProviderType.GOOGLE);

		given(guestService.createNewGuestFrom(info))
			.willReturn(new GuestInfo("guestUuid", "email", "userName", ProviderType.GOOGLE, "pictureUrl"));

		LoginInfo guestInfo = sut.createGuestInfoFrom(info);

		assertThat(guestInfo.guestUuid()).isEqualTo("guestUuid");
		assertThat(guestInfo.email()).isEqualTo("email");
		assertThat(guestInfo.name()).isEqualTo("userName");
		assertThat(guestInfo.profileImageUrl()).isEqualTo("pictureUrl");
		assertThat(guestInfo.providerType()).isEqualTo(ProviderType.GOOGLE);

	}



}