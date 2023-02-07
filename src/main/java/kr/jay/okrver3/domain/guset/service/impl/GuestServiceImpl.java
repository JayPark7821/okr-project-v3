package kr.jay.okrver3.domain.guset.service.impl;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.guset.service.GuestStore;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

	private final GuestStore guestStore;

	@Override
	public GuestInfo createNewGuestFrom(OAuth2UserInfo info) {
		return null;
	}
}
