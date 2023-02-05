package kr.jay.okrver3.application.user;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	public LoginInfo getLoginInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		throw new UnsupportedOperationException("kr.jay.okrver3.application.user.UserFacade.getLoginInfoFrom()");
	}
}


