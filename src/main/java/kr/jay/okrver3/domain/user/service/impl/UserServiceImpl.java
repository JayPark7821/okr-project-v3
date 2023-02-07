package kr.jay.okrver3.domain.user.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.user.UserInfo;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	@Override
	public Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return Optional.empty();
	}
}
