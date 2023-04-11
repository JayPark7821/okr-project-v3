package kr.service.user.api;

import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.RoleType;

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
