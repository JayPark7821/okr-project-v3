package kr.jay.okrver3.interfaces.user;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.info.JobInfo;
import kr.jay.okrver3.domain.user.info.LoginInfo;
import kr.jay.okrver3.interfaces.user.response.JobResponse;
import kr.jay.okrver3.interfaces.user.response.LoginResponse;
import kr.jay.okrver3.interfaces.user.response.TokenResponse;
import kr.jay.okrver3.interfaces.user.response.UserInfoResponse;

@Component
public class UserDtoMapper {

	public LoginResponse of(LoginInfo info) {
		return new LoginResponse(
			info.guestUuid(),
			info.email(),
			info.name(),
			info.providerType(),
			RoleType.USER,
			info.profileImageUrl(),
			info.accessToken(),
			info.refreshToken(),
			info.jobFieldDetail()
		);

	}

	public TokenResponse of(AuthTokenInfo info) {
		return new TokenResponse(
			info.accessToken(),
			info.refreshToken()
		);
	}

	public JobResponse of(JobInfo info) {
		return new JobResponse(
			info.code(),
			info.title()
		);
	}

	public UserInfoResponse of(User user) {
		return new UserInfoResponse(
			user.getEmail(),
			user.getUsername(),
			user.getProviderType(),
			user.getRoleType(),
			user.getJobField().getTitle(),
			user.getProfileImage()
		);
	}
}
