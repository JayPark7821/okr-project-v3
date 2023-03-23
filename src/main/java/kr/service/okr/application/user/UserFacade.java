package kr.service.okr.application.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.okr.domain.guset.service.GuestService;
import kr.service.okr.domain.project.ProjectService;
import kr.service.okr.domain.token.service.AuthTokenInfo;
import kr.service.okr.domain.token.service.TokenService;
import kr.service.okr.domain.user.JobCategory;
import kr.service.okr.domain.user.UserInfoUpdateCommand;
import kr.service.okr.domain.user.info.JobInfo;
import kr.service.okr.domain.user.info.LoginInfo;
import kr.service.okr.domain.user.info.UserInfo;
import kr.service.okr.domain.user.service.UserService;
import kr.service.okr.infrastructure.user.auth.OAuth2UserInfo;
import kr.service.okr.interfaces.user.request.JoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final GuestService guestService;
	private final TokenService tokenService;
	private final ProjectService projectService;

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

	public AuthTokenInfo getNewAccessToken(String refreshToken) {
		return tokenService.getNewAccessToken(refreshToken);
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

	public void unRegisterUser(final Long userSeq) {
		userService.makeUserAsUnknownUser(userSeq);
		projectService.promoteNextProjectLeader(userSeq);
	}
}



