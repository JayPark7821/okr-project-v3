package kr.service.okr.application.user;

import kr.service.okr.user.guest.domain.Guest;
import kr.service.okr.user.token.usecase.GenerateTokenSetUseCase;
import kr.service.okr.user.user.domain.User;
import kr.service.user.ProviderType;

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
	public LoginInfo(User userInfo, GenerateTokenSetUseCase.AuthTokenInfo authTokenInfo) {
		this(
			null,
			userInfo.getEmail(),
			userInfo.getUsername(),
			userInfo.getProviderType(),
			userInfo.getProfileImage(),
			authTokenInfo.accessToken(),
			authTokenInfo.refreshToken(),
			userInfo.getJobField().getTitle()
		);
	}

	public LoginInfo(Guest guest) {
		this(guest.getGuestUuid(), guest.getEmail(), guest.getGuestName(), guest.getProviderType(),
			guest.getProfileImage(), null, null, null);
	}

}
