package kr.service.okr.domain.guset.service;

import kr.service.okr.domain.guset.Guest;
import kr.service.okr.domain.user.ProviderType;

public record GuestInfo(String guestUuid, String guestId, String email, String name, ProviderType providerType,
						String profileImageUrl) {

	public GuestInfo(Guest guest) {

		this(guest.getGuestUuid(), guest.getGuestId(), guest.getEmail(), guest.getGuestName(), guest.getProviderType(),
			guest.getProfileImage());
	}
}
