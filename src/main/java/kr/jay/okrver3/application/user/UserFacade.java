package kr.jay.okrver3.application.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.TokenService;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.domain.user.UserInfoUpdateCommand;
import kr.jay.okrver3.domain.user.info.JobInfo;
import kr.jay.okrver3.domain.user.info.LoginInfo;
import kr.jay.okrver3.domain.user.info.UserInfo;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.request.UserInfoUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final GuestService guestService;
	private final TokenService tokenService;

	public Optional<LoginInfo> getLoginInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return userService.getUserInfoFrom(oAuth2UserInfo)
			.map(info -> new LoginInfo(info, tokenService.generateTokenSet(info)));
	}

	public LoginInfo createGuestInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return new LoginInfo(guestService.createNewGuestFrom(oAuth2UserInfo));
	}

	public LoginInfo join(JoinRequest joinRequest) {

		UserInfo userInfo = userService.registerNewUserFrom(
			guestService.getGuestInfoFrom(joinRequest.guestUserId()),
			joinRequest
		);

		return new LoginInfo(userInfo, tokenService.generateTokenSet(userInfo));
	}

	public String validateEmail(String email, Long userFromAuthentication) {
		return userService.findUserInfoBy(email).email();
	}

	public AuthTokenInfo getNewAccessToken(String accessToken) {
		return tokenService.getNewAccessToken(accessToken);
	}

	public List<JobInfo> getJobCategory() {
		return userService.getJobCategory();
	}

	public List<JobInfo> getJobField(JobCategory category) {
		return userService.getJobField(category);
	}

	public void updateUserInfo(UserInfoUpdateCommand command, Long userSeq) {
		userService.updateUserInfo(command, userSeq);
	}
}


