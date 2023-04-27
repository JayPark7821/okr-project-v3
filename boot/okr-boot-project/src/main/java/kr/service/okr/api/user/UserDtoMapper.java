package kr.service.okr.api.user;

import kr.service.okr.user.api.JobResponse;
import kr.service.okr.user.api.LoginResponse;
import kr.service.okr.user.usecase.user.JobInfo;
import kr.service.okr.user.usecase.user.LoginInfo;

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

	public static JobResponse of(JobInfo jobInfo) {
		return new JobResponse(
			jobInfo.code(),
			jobInfo.title());
	}
}
