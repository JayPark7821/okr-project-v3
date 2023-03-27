package kr.service.okr.interfaces.user.response;

import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.domain.user.RoleType;

public record UserInfoResponse(
	String email,
	String name,
	ProviderType providerType,
	RoleType roleType,
	String jobFieldDetail,
	String profileImage
) {
}
