package kr.service.user.token.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.jwt.JwtService;
import kr.service.user.token.domain.RefreshToken;
import kr.service.user.token.repository.RefreshTokenCommand;
import kr.service.user.token.repository.RefreshTokenQuery;
import kr.service.user.token.usecase.GenerateTokenSetUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateTokenSet implements GenerateTokenSetUseCase {

	private final RefreshTokenCommand command;
	private final RefreshTokenQuery query;
	private final JwtService jwtService;

	@Override
	public AuthTokenInfo command(final String email) {
		final Optional<RefreshToken> tokenOptional = query.findByUserEmail(email);
		final RefreshToken refreshToken;

		if (tokenOptional.isPresent()) {
			refreshToken = tokenOptional.get();
			if (jwtService.needNewRefreshToken(refreshToken.getRefreshToken())) {
				refreshToken.updateRefreshToken(jwtService.generateJwtToken(email, true));
				command.save(refreshToken);
			}
		} else {
			refreshToken = command.save(new RefreshToken(email, jwtService.generateJwtToken(email, true)));
		}
		return new AuthTokenInfo(jwtService.generateJwtToken(email, false), refreshToken.getRefreshToken());
	}
}
