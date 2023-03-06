package kr.jay.okrver3.interfaces.user.response;

import kr.jay.okrver3.domain.user.ProviderType;

public record LoginResponse(String guestId, String email, String name, ProviderType providerType, String accessToken,
							String refreshToken, String jobFieldDetail) {

}
