package kr.jay.okrver3.interfaces.user.response;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;

public record LoginResponse(String guestUserId, String email, String name, ProviderType providerType, RoleType roleType,String profileImage, String accessToken,
							String refreshToken, String jobFieldDetail) {

}
