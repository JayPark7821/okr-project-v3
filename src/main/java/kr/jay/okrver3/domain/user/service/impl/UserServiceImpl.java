package kr.jay.okrver3.domain.user.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.domain.user.JobField;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.info.JobInfo;
import kr.jay.okrver3.domain.user.info.UserInfo;
import kr.jay.okrver3.domain.user.service.UserRepository;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
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
			.jobField(JobField.lookup(joinRequest.jobField()))
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
	public List<JobInfo> getJobField(JobCategory category){
		return null;
	}
}
