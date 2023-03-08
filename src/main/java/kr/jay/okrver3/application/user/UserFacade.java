package kr.jay.okrver3.application.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.TokenService;
import kr.jay.okrver3.domain.user.service.LoginInfo;
import kr.jay.okrver3.domain.user.service.UserInfo;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
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

	public LoginInfo join(JoinRequest joinRequest) {

		UserInfo userInfo = userService.registerNewUserFrom(
			guestService.getGuestInfoFrom(joinRequest.guestTempId()),
			joinRequest
		);

		return new LoginInfo(userInfo, tokenService.generateTokenSet(userInfo));
	}

	public AuthTokenInfo getRefreshToken(HttpServletRequest request) {
		return null;
	}
}


