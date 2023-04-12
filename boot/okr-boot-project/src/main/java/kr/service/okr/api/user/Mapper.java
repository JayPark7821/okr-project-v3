package kr.service.okr.api.user;

import kr.service.okr.application.user.LoginInfo;
import kr.service.user.api.LoginResponse;

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
