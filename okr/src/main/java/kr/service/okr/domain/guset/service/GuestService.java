package kr.service.okr.domain.guset.service;

import kr.service.okr.infrastructure.user.auth.OAuth2UserInfo;

public interface GuestService {
	GuestInfo createNewGuestFrom(OAuth2UserInfo info);

	GuestInfo getGuestInfoFrom(String guestUserId);
}
