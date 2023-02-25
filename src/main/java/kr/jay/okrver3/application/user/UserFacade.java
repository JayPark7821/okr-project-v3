package kr.jay.okrver3.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.token.service.TokenService;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final GuestService guestService;
	private final TokenService tokenService;

	public Optional<LoginInfo> getLoginInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return userService.getUserInfoFrom(oAuth2UserInfo)
			.map(info -> new LoginInfo(info, tokenService.generateTokenSet(info)));
	}

	public LoginInfo createGuestInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return new LoginInfo(guestService.createNewGuestFrom(oAuth2UserInfo));
	}

	public LoginInfo join(JoinRequestDto joinRequestDto) {
		throw new UnsupportedOperationException("kr.jay.okrver3.application.user.UserFacade.join()");
	}
}


