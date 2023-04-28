package kr.service.okr.api.user;

import kr.service.okr.user.api.JobResponse;
import kr.service.okr.user.api.LoginResponse;
import kr.service.okr.user.api.TokenResponse;
import kr.service.okr.user.usecase.token.TokenInfo;
import kr.service.okr.user.usecase.user.JobInfo;
import kr.service.okr.user.usecase.user.LoginInfo;

public class UserDtoMapper {

	static LoginResponse of(final LoginInfo loginInfo) {
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

	static JobResponse of(final JobInfo jobInfo) {
		return new JobResponse(
			jobInfo.code(),
			jobInfo.title());
	}

	static TokenResponse of(final TokenInfo tokenInfo) {
		return new TokenResponse(
			tokenInfo.accessToken(),
			tokenInfo.refreshToken()
		);
	}
}
