package kr.jay.okrver3.domain.user.service;

import java.util.Optional;

import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

public interface UserService {
	Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo);
}
