package kr.jay.okrver3.interfaces.user;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.user.service.LoginInfo;
import kr.jay.okrver3.interfaces.user.response.LoginResponse;

@Component
public class UserDtoMapper {

	public LoginResponse of(LoginInfo info) {
		return new LoginResponse(
			info.guestUuid(),
			info.email(),
			info.name(),
			info.providerType(),
			info.accessToken(),
			info.refreshToken(),
			info.jobFieldDetail()
		);

	}
}
