package kr.jay.okrver3.application.user;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.UserInfo;

public record LoginInfo(String guestUuid, String email, String name, ProviderType providerType, String profileImageUrl, String accessToken,
						String refreshToken) {

	public LoginInfo(UserInfo userInfo) {
		this(null , userInfo.email(), userInfo.name(), userInfo.providerType(), userInfo.picture(), null, null);
	}
}
