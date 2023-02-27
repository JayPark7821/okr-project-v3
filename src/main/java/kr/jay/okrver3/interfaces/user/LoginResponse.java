package kr.jay.okrver3.interfaces.user;

import kr.jay.okrver3.application.user.LoginInfo;
import kr.jay.okrver3.domain.user.ProviderType;

public record LoginResponse(String guestId, String email, String name, ProviderType providerType, String accessToken,
							String refreshToken, String jobFieldDetail){

	public LoginResponse(LoginInfo info) {
		this(info.guestUuid(), info.email(), info.name(), info.providerType(), info.accessToken(), info.refreshToken(), info.jobFieldDetail());
	}
}
