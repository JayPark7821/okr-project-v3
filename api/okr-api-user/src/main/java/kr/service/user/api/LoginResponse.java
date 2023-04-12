package kr.service.user.api;

import kr.service.user.ProviderType;

public record LoginResponse(
	String guestUserId,
	String email,
	String name,
	ProviderType providerType,
	String profileImage,
	String accessToken,
	String refreshToken,
	String jobFieldDetail
) {

}
