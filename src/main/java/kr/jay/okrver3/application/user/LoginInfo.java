package kr.jay.okrver3.application.user;

import kr.jay.okrver3.domain.user.ProviderType;

public record LoginInfo(String guestUuid, String email, String name, ProviderType providerType, String accessToken,
						String refreshToken) {
}
