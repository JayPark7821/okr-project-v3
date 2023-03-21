package kr.service.okr.domain.user.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.domain.guset.service.GuestInfo;
import kr.service.okr.domain.user.JobCategory;
import kr.service.okr.domain.user.JobField;
import kr.service.okr.domain.user.RoleType;
import kr.service.okr.domain.user.User;
import kr.service.okr.domain.user.UserInfoUpdateCommand;
import kr.service.okr.domain.user.info.JobInfo;
import kr.service.okr.domain.user.info.UserInfo;
import kr.service.okr.domain.user.service.UserRepository;
import kr.service.okr.domain.user.service.UserService;
import kr.service.okr.infrastructure.user.auth.OAuth2UserInfo;
import kr.service.okr.interfaces.user.request.JoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public Optional<UserInfo> getUserInfoFrom(OAuth2UserInfo oAuth2UserInfo) {
		return userRepository.findByEmail(oAuth2UserInfo.email())
			.map(user -> {
				user.validateProvider(oAuth2UserInfo.providerType());
				return new UserInfo(user);
			});
	}

	@Override
	public UserInfo findUserInfoBy(String email) {
		return userRepository.findByEmail(email)
			.map(UserInfo::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL));
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserInfo registerNewUserFrom(GuestInfo guestInfo, JoinRequest joinRequest) {
		userRepository.findByEmail(guestInfo.email())
			.ifPresent(user -> {
				throw new OkrApplicationException(ErrorCode.ALREADY_JOINED_USER);
			});

		User user = User.builder()
			.userId(guestInfo.guestId())
			.email(guestInfo.email())
			.username(joinRequest.name())
			.roleType(RoleType.USER)
			.profileImage(guestInfo.profileImageUrl())
			.providerType(guestInfo.providerType())
			.jobField(JobField.of(joinRequest.jobField()))
			.build();

		return new UserInfo(userRepository.save(user));
	}

	@Override
	public List<JobInfo> getJobCategory() {
		return Arrays.stream(JobCategory.values())
			.map(jobCategory -> new JobInfo(jobCategory.getCode(), jobCategory.getTitle()))
			.toList();
	}

	@Override
	public List<JobInfo> getJobField(JobCategory category) {
		return category.getDetailList().stream()
			.map(jobField -> new JobInfo(jobField.getCode(), jobField.getTitle()))
			.toList();
	}

	@Transactional
	@Override
	public void updateUserInfo(UserInfoUpdateCommand command, Long userSeq) {
		User user = userRepository.findById(userSeq)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));

		user.updateUserName(command.userName());
		user.updateJobField(JobField.of(command.jobField()));
		userRepository.save(user);
	}

	@Transactional
	@Override
	public void makeUserAsUnknownUser(final Long userSeq) {
		User user = userRepository.findById(userSeq)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));

		user.makeAsUnknownUser();
	}
}
