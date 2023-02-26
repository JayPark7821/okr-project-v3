package kr.jay.okrver3.domain.user.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.JobFieldDetail;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserInfo;
import kr.jay.okrver3.domain.user.service.UserRepository;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.JoinRequestDto;
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
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserInfo registerNewUserFrom(GuestInfo guestInfo, JoinRequestDto joinRequestDto) {
		userRepository.findByEmail(guestInfo.email())
			.ifPresent(user -> {
				throw new OkrApplicationException(ErrorCode.ALREADY_JOINED_USER);
			});

		User user = User.builder()
			.userId(guestInfo.guestId())
			.email(guestInfo.email())
			.username(joinRequestDto.name())
			.roleType(RoleType.USER)
			.profileImage(guestInfo.profileImageUrl())
			.providerType(guestInfo.providerType())
			.jobField(JobFieldDetail.lookup(joinRequestDto.jobField()))
			.build();

		return new UserInfo(userRepository.save(user));
	}
}
