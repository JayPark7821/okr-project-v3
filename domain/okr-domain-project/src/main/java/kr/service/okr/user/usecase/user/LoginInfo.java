package kr.service.okr.user.usecase.user;

import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.enums.ProviderType;

public record LoginInfo(
	String guestUuid,
	String email,
	String name,
	ProviderType providerType,
	String profileImageUrl,
	String accessToken,
	String refreshToken,
	String jobFieldDetail
) {
	public LoginInfo(User userInfo, String accessToken, String refreshToken) {
		this(
			null,
			userInfo.getEmail(),
			userInfo.getUsername(),
			userInfo.getProviderType(),
			userInfo.getProfileImage(),
			accessToken,
			refreshToken,
			userInfo.getJobField().getTitle()
		);
	}

	public LoginInfo(Guest guest) {
		this(guest.getGuestUuid(), guest.getEmail(), guest.getGuestName(), guest.getProviderType(),
			guest.getProfileImage(), null, null, null);
	}

}
