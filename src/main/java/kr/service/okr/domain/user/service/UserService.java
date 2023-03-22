package kr.service.okr.domain.user.service;

import java.util.List;
import java.util.Optional;

import kr.service.okr.domain.guset.service.GuestInfo;
import kr.service.okr.domain.user.JobCategory;
import kr.service.okr.domain.user.User;
import kr.service.okr.domain.user.UserInfoUpdateCommand;
import kr.service.okr.domain.user.info.JobInfo;
import kr.service.okr.domain.user.info.UserInfo;
import kr.service.okr.infrastructure.user.auth.OAuth2UserInfo;
import kr.service.okr.interfaces.user.request.JoinRequest;

public interface UserService {
	Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo);

	UserInfo findUserInfoBy(String email);

	Optional<User> findUserByEmail(String email);

	UserInfo registerNewUserFrom(GuestInfo guestInfo, JoinRequest joinRequest);

	List<JobInfo> getJobCategory();

	List<JobInfo> getJobField(JobCategory category);

	void updateUserInfo(UserInfoUpdateCommand command, Long userSeq);

	void makeUserAsUnknownUser(Long userSeq);
}
