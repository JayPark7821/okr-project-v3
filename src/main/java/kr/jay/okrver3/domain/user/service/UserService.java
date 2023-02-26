package kr.jay.okrver3.domain.user.service;

import java.util.Optional;

import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.JoinRequestDto;

public interface UserService {
	Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo);

	Optional<User> findByEmail(String email);

	UserInfo registerNewUserFrom(GuestInfo guestInfo, JoinRequestDto joinRequestDto);

}
