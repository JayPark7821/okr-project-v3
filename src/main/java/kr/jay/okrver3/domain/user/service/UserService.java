package kr.jay.okrver3.domain.user.service;

import java.util.Optional;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;

public interface UserService {
	Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo);

	Optional<User> findByEmail(String email);
}
