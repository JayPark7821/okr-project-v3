package kr.service.okr.user.token.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.token.domain.RefreshToken;
import kr.service.okr.user.token.repository.AuthService;
import kr.service.okr.user.token.repository.RefreshTokenCommand;
import kr.service.okr.user.token.repository.RefreshTokenQuery;
import kr.service.okr.user.token.usecase.GenerateTokenSetUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateTokenSet implements GenerateTokenSetUseCase {

	private final RefreshTokenCommand command;
	private final RefreshTokenQuery query;
	private final AuthService authService;

	@Override
	public AuthTokenInfo command(final String email) {
		final Optional<RefreshToken> tokenOptional = query.findByUserEmail(email);
		final RefreshToken refreshToken;

		if (tokenOptional.isPresent()) {
			refreshToken = tokenOptional.get();
			if (authService.needNewAuthentication(refreshToken.getRefreshToken())) {
				refreshToken.updateRefreshToken(authService.generateAuthToken(email, true));
				command.save(refreshToken);
			}
		} else {
			refreshToken = command.save(new RefreshToken(email, authService.generateAuthToken(email, true)));
		}
		return new AuthTokenInfo(authService.generateAuthToken(email, false), refreshToken.getRefreshToken());
	}
}
