package kr.service.okr.user.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.repository.token.AuthenticationRepository;
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
	private final AuthenticationRepository authenticationRepository;

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
				authenticationRepository.generateAccessToken(user.getEmail()),
				getRefreshToken(user).getRefreshToken()
			)
		);
	}

	private RefreshToken getRefreshToken(final User user) {
		final Optional<RefreshToken> tokenOptional = refreshTokenQuery.findByUserEmail(user.getEmail());
		final RefreshToken refreshToken;

		if (tokenOptional.isPresent()) {
			refreshToken = tokenOptional.get();
			if (authenticationRepository.isTokenExpired(refreshToken.getRefreshToken())) {
				refreshToken.updateRefreshToken(authenticationRepository.generateRefreshToken(user.getEmail()));
				refreshTokenCommand.save(refreshToken);
			}
		} else {
			refreshToken = refreshTokenCommand.save(
				new RefreshToken(user.getEmail(), authenticationRepository.generateRefreshToken(user.getEmail())));
		}
		return refreshToken;
	}
}
