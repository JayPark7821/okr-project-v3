package kr.jay.okrver3.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final GuestService guestService;

	public Optional<LoginInfo> getLoginInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		throw new UnsupportedOperationException("kr.jay.okrver3.application.user.UserFacade.getLoginInfoFrom()");
	}

	public LoginInfo createGuestInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		throw new UnsupportedOperationException("kr.jay.okrver3.application.user.UserFacade.createGuestInfoFrom())");
	}
}


