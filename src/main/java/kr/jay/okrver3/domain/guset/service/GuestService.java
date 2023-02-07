package kr.jay.okrver3.domain.guset.service;

import kr.jay.okrver3.interfaces.user.auth.OAuth2UserInfo;

public interface GuestService {
	GuestInfo createNewGuestFrom(OAuth2UserInfo info);
}
