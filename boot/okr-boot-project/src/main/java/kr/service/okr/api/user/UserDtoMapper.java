package kr.service.okr.api.user;

import kr.service.okr.application.user.LoginInfo;
import kr.service.okr.user.api.LoginResponse;

public class UserDtoMapper {

	public static LoginResponse of(LoginInfo loginInfo) {
		return new LoginResponse(
			loginInfo.guestUuid(),
			loginInfo.email(),
			loginInfo.name(),
			loginInfo.providerType().name(),
			loginInfo.profileImageUrl(),
			loginInfo.accessToken(),
			loginInfo.refreshToken(),
			loginInfo.jobFieldDetail()
		);
	}
}
