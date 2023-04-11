package kr.service.user.api;

import kr.service.user.guest.ProviderType;
import kr.service.user.user.domain.RoleType;

public record LoginResponse(
	String guestUserId,
	String email,
	String name,
	ProviderType providerType,
	RoleType roleType,
	String profileImage,
	String accessToken,
	String refreshToken,
	String jobFieldDetail
) {

}
