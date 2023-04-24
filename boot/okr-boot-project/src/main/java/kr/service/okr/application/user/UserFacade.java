package kr.service.okr.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.jwt.JwtTokenRepository;
import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.auth.usecase.GenerateTokenSetUseCase;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.guest.usecase.JoinNewGuestUseCase;
import kr.service.okr.user.user.domain.User;
import kr.service.okr.user.user.usecase.QueryUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {

	private final QueryUserUseCase queryUserUseCase;
	private final GenerateTokenSetUseCase GenerateTokenSetUseCase;
	private final JoinNewGuestUseCase joinNewGuestUseCase;
	private final JwtTokenRepository jwtService;

	public Optional<LoginInfo> getLoginInfoFrom(final OAuth2UserInfo info) {
		final Optional<User> selectedUser = queryUserUseCase.query(info.email());

		return selectedUser.isEmpty() ?
			Optional.empty() :
			Optional.of(
				new LoginInfo(
					selectedUser.filter(user -> user.validateProvider(ProviderType.of(info.socialPlatform())))
						.orElseThrow(
							() -> new OkrApplicationException(ErrorCode.MISS_MATCH_PROVIDER, info.socialPlatform())),
					GenerateTokenSetUseCase.command(info.email())));

	}

	public LoginInfo createGuest(final OAuth2UserInfo info) {
		return new LoginInfo(joinNewGuestUseCase.command(createCommand(info)));
	}

	private JoinNewGuestUseCase.Command createCommand(final OAuth2UserInfo info) {
		return new JoinNewGuestUseCase.Command(
			info.id(),
			info.username(),
			info.email(),
			info.profileImage(),
			ProviderType.of(info.socialPlatform())
		);
	}

}
