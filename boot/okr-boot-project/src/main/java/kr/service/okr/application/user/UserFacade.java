package kr.service.okr.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.jwt.JwtTokenRepository;
import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.api.JoinRequest;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.usecase.guest.JoinNewGuestUseCase;
import kr.service.okr.user.usecase.token.GenerateTokenSetUseCase;
import kr.service.okr.user.usecase.user.QueryUserUseCase;
import kr.service.okr.user.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {

	private final QueryUserUseCase queryUserUseCase;
	private final GenerateTokenSetUseCase GenerateTokenSetUseCase;
	private final RegisterUserUseCase registerUserUseCase;
	private final JoinNewGuestUseCase joinNewGuestUseCase;
	private final JwtTokenRepository jwtService;

	public Optional<LoginInfo> getLoginInfoFrom(final OAuth2UserInfo info) {
		final Optional<User> selectedUser = queryUserUseCase.query(info.email());
		return selectedUser.isEmpty() ? Optional.empty() : validateProviderAndGetLoginInfo(info, selectedUser);
	}

	public LoginInfo createGuest(final OAuth2UserInfo info) {
		return new LoginInfo(joinNewGuestUseCase.command(toCommand(info)));
	}

	private Optional<LoginInfo> validateProviderAndGetLoginInfo(
		final OAuth2UserInfo info,
		final Optional<User> selectedUser
	) {
		return Optional.of(
			new LoginInfo(
				selectedUser.filter(user -> user.validateProvider(ProviderType.of(info.socialPlatform())))
					.orElseThrow(
						() -> new OkrApplicationException(ErrorCode.MISS_MATCH_PROVIDER, info.socialPlatform())),
				GenerateTokenSetUseCase.command(info.email())));
	}

	public LoginInfo join(final JoinRequest joinRequest) {
		User user = registerUserUseCase.command(toCommand(joinRequest));
		return new LoginInfo(user, GenerateTokenSetUseCase.command(user.getEmail()));
	}

	private RegisterUserUseCase.Command toCommand(final JoinRequest joinRequest) {
		return new RegisterUserUseCase.Command(
			joinRequest.guestUuid(),
			joinRequest.username(),
			joinRequest.email(),
			joinRequest.jobField()
		);
	}

	private JoinNewGuestUseCase.Command toCommand(final OAuth2UserInfo info) {
		return new JoinNewGuestUseCase.Command(
			info.id(),
			info.username(),
			info.email(),
			info.profileImage(),
			ProviderType.of(info.socialPlatform())
		);
	}

}
