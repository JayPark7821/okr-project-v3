package kr.jay.okrver3.domain.user.service;

import java.util.List;
import java.util.Optional;

import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.UserInfoUpdateCommand;
import kr.jay.okrver3.domain.user.info.JobInfo;
import kr.jay.okrver3.domain.user.info.UserInfo;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;

public interface UserService {
	Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo);

	UserInfo findUserInfoBy(String email);

	Optional<User> findUserByEmail(String email);

	UserInfo registerNewUserFrom(GuestInfo guestInfo, JoinRequest joinRequest);

	List<JobInfo> getJobCategory();

	List<JobInfo> getJobField(JobCategory category);

	void updateUserInfo(UserInfoUpdateCommand command, Long userSeq);
}
