package kr.jay.okrver3.interfaces.user.auth;

import kr.jay.okrver3.domain.guset.Guest;
import kr.jay.okrver3.domain.user.ProviderType;

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