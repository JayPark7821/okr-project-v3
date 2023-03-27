package kr.service.okr.interfaces.user.response;

import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.domain.user.RoleType;

public record LoginResponse(String guestUserId, String email, String name, ProviderType providerType, RoleType roleType,String profileImage, String accessToken,
							String refreshToken, String jobFieldDetail) {

}
