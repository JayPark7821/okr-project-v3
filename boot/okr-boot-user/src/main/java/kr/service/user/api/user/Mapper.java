package kr.service.user.api.user;

import kr.service.user.api.LoginResponse;
import kr.service.user.application.user.LoginInfo;

public class Mapper {

	public static LoginResponse of(LoginInfo loginInfo) {
		return new LoginResponse(
			loginInfo.guestUuid(),
			loginInfo.email(),
			loginInfo.name(),
			loginInfo.providerType(),
			loginInfo.profileImageUrl(),
			loginInfo.accessToken(),
			loginInfo.refreshToken(),
			loginInfo.jobFieldDetail()
		);
	}
}
