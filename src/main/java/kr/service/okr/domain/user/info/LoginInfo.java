package kr.service.okr.domain.user.info;

import kr.service.okr.domain.guset.service.GuestInfo;
import kr.service.okr.domain.token.service.AuthTokenInfo;
import kr.service.okr.domain.user.ProviderType;

public record LoginInfo(String guestUuid, String email, String name, ProviderType providerType, String profileImageUrl,
						String accessToken,
						String refreshToken, String jobFieldDetail) {

	public LoginInfo(UserInfo userInfo, AuthTokenInfo authTokenInfo) {
		this(null, userInfo.email(), userInfo.name(), userInfo.providerType(), userInfo.profileImageUrl(),
			authTokenInfo.accessToken(), authTokenInfo.refreshToken(), userInfo.jobField().getCode());
	}

	public LoginInfo(GuestInfo guestInfo) {
		this(guestInfo.guestUuid(), guestInfo.email(), guestInfo.name(), guestInfo.providerType(),
			guestInfo.profileImageUrl(), null, null, null);
	}

}
