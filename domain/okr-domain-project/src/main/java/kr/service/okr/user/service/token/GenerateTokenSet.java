package kr.service.okr.user.service.token;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.repository.token.AuthenticationRepository;
import kr.service.okr.user.repository.token.RefreshTokenCommand;
import kr.service.okr.user.repository.token.RefreshTokenQuery;
import kr.service.okr.user.usecase.token.GenerateTokenSetUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateTokenSet implements GenerateTokenSetUseCase {

	private final RefreshTokenCommand command;
	private final RefreshTokenQuery query;
	private final AuthenticationRepository authenticationRepository;

	@Override
	public AuthTokenInfo command(final String email) {
		final Optional<RefreshToken> tokenOptional = query.findByUserEmail(email);
		final RefreshToken refreshToken;

		if (tokenOptional.isPresent()) {
			refreshToken = tokenOptional.get();
			if (authenticationRepository.isTokenExpired(refreshToken.getRefreshToken())) {
				refreshToken.updateRefreshToken(authenticationRepository.generateRefreshToken(email));
				command.save(refreshToken);
			}
		} else {
			refreshToken = command.save(
				new RefreshToken(email, authenticationRepository.generateRefreshToken(email)));
		}
		return new AuthTokenInfo(
			authenticationRepository.generateAccessToken(email),
			refreshToken.getRefreshToken()
		);
	}
}
