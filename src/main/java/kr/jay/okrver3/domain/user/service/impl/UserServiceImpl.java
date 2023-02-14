package kr.jay.okrver3.domain.user.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.user.service.UserInfo;
import kr.jay.okrver3.domain.user.service.UserReader;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;

	@Override
	public Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return userReader.findByEmail(oAuth2UserInfo.email())
			.map(user -> {
				user.validateProvider(oAuth2UserInfo.providerType());
				return new UserInfo(user);
			});
	}
}
