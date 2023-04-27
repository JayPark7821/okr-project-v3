package kr.service.okr.user.service.user;

import static kr.service.okr.user.domain.AuthenticationProvider.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.repository.token.RefreshTokenCommand;
import kr.service.okr.user.repository.token.RefreshTokenQuery;
import kr.service.okr.user.repository.user.UserQuery;
import kr.service.okr.user.usecase.user.LoginInfo;
import kr.service.okr.user.usecase.user.ProcessLoginUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProcessLogin implements ProcessLoginUseCase {

	private final UserQuery userQuery;
	private final RefreshTokenCommand refreshTokenCommand;
	private final RefreshTokenQuery refreshTokenQuery;

	@Override
	public Optional<LoginInfo> command(final Command command) {
		return userQuery.findByEmail(command.email())
			.flatMap(value -> processLogin(command.providerType(), value));
	}

	private Optional<LoginInfo> processLogin(
		final ProviderType providerType,
		final User user
	) {
		user.validateProvider(providerType);
		return Optional.of(
			new LoginInfo(
				user,
				generateAccessToken(user.getEmail()),
				getRefreshToken(user).getRefreshToken()
			)
		);
	}

	private RefreshToken getRefreshToken(final User user) {
		return refreshTokenCommand.save(refreshTokenQuery.findByUserEmail(user.getEmail())
			.map(RefreshToken::checkAndRenewToken)
			.orElse(RefreshToken.generateNewRefreshToken(user.getEmail())));
	}
}
