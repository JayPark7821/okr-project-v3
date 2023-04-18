package kr.service.okr.api.user;

import kr.service.okr.user.user.domain.User;
import kr.service.user.api.LoginResponse;
import kr.service.user.api.internal.UserInfoResponse;
import kr.service.user.application.user.LoginInfo;

public class Mapper {

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

	public static UserInfoResponse of(User user) {
		return new UserInfoResponse(
			user.getUserSeq(),
			user.getEmail(),
			user.getUserId(),
			user.getUsername()
		);
	}
}
