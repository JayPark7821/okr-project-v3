package kr.service.okr.infrastructure.user.auth;

import kr.service.okr.domain.guset.Guest;
import kr.service.okr.domain.user.ProviderType;

public record OAuth2UserInfo(String id, String name, String email, String picture, ProviderType providerType) {

	public Guest toGuest() {
		return Guest.builder()
			.guestId(id)
			.guestName(name)
			.email(email)
			.providerType(providerType)
			.profileImage(picture)
			.build();
	}
}