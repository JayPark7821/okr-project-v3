package kr.jay.okrver3.domain.guset.service;

import java.util.Optional;

import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;

public interface GuestService {
	GuestInfo createNewGuestFrom(OAuth2UserInfo info);

	Optional<GuestInfo> getGuestInfoFrom(String guestTempId);
}
