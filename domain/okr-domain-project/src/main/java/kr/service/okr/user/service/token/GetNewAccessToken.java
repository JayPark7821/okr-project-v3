package kr.service.okr.user.service.token;

import static kr.service.okr.user.domain.AuthenticationProvider.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.repository.token.RefreshTokenCommand;
import kr.service.okr.user.repository.token.RefreshTokenQuery;
import kr.service.okr.user.usecase.token.GetNewAccessTokenUseCase;
import kr.service.okr.user.usecase.token.TokenInfo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GetNewAccessToken implements GetNewAccessTokenUseCase {

	private final RefreshTokenQuery refreshTokenQuery;
	private final RefreshTokenCommand refreshTokenCommand;

	@Override
	public TokenInfo command(final String email) {
		final RefreshToken savedRefreshToken =
			refreshTokenCommand.save(getRefreshTokenAndUpdateBy(email));

		return new TokenInfo(
			generateAccessToken(email),
			savedRefreshToken.getRefreshToken()
		);
	}

	private RefreshToken getRefreshTokenAndUpdateBy(final String email) {
		return refreshTokenQuery.findByUserEmail(email)
			.map(RefreshToken::checkAndRenewToken)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_TOKEN));
	}
}
