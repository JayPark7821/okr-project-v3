package kr.jay.okrver3.domain.user.service;

import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.user.ProviderType;

public record LoginInfo(String guestUuid, String email, String name, ProviderType providerType, String profileImageUrl,
						String accessToken,
						String refreshToken, String jobFieldDetail) {

	public LoginInfo(UserInfo userInfo, AuthTokenInfo authTokenInfo) {
		this(null, userInfo.email(), userInfo.name(), userInfo.providerType(), userInfo.profileImageUrl(),
			authTokenInfo.accessToken(), authTokenInfo.refreshToken(), userInfo.jobFieldDetail().getCode());
	}

	public LoginInfo(GuestInfo guestInfo) {
		this(guestInfo.guestUuid(), guestInfo.email(), guestInfo.name(), guestInfo.providerType(),
			guestInfo.profileImageUrl(), null, null, null);
	}

}
