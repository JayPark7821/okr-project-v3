package kr.jay.okrver3.domain.token.service.impl;

import static kr.jay.okrver3.common.utils.JwtTokenUtils.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.RefreshTokenRepository;
import kr.jay.okrver3.domain.token.service.TokenService;
import kr.jay.okrver3.domain.user.service.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	@Value("${app.auth.tokenSecret}")
	private String secretKey;
	@Value("${app.auth.refreshTokenExpiry}")
	private Long refreshExpiredTimeMs;
	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;

	@Value("${app.auth.refreshTokenRegenerationThreshold}")
	private Long refreshTokenRegenerationThreshold;

	@Override
	public AuthTokenInfo generateTokenSet(UserInfo userInfo) {

		RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(userInfo.email())
			.orElseGet(() -> refreshTokenRepository.save(getRefreshToken(userInfo.email())));

		if (getRemainingTimeOf(refreshToken.getRefreshToken()) <= refreshTokenRegenerationThreshold)
			refreshToken.updateRefreshToken(createNewToken(userInfo.email(), refreshExpiredTimeMs));

		return new AuthTokenInfo(createNewToken(userInfo.email(), accessExpiredTimeMs), refreshToken.getRefreshToken());
	}


	@Override
	public AuthTokenInfo getNewAccessToken(String refreshToken) {
		String email = JwtTokenUtils.getEmail(refreshToken, secretKey);

		refreshTokenRepository.findByEmailAndRefreshToken(email, refreshToken)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_TOKEN));

		Date now = new Date();
		String newAccessToken = JwtTokenUtils.generateToken(email, secretKey, accessExpiredTimeMs);
		if (getRemainingTimeOf(refreshToken) <= refreshTokenRegenerationThreshold) {
			return new AuthTokenInfo(newAccessToken, createNewToken(email, refreshExpiredTimeMs));
		} else {
			return new AuthTokenInfo(newAccessToken, refreshToken);
		}
	}

	private long getRemainingTimeOf(String refreshToken) {
		return getExpiration(refreshToken, secretKey).getTime() - new Date().getTime();
	}

	private String createNewToken(String email, Long expiredTimeMs) {
		return generateToken(email, secretKey, expiredTimeMs);
	}

	private RefreshToken getRefreshToken(String email) {
		return new RefreshToken(email, createNewToken(email, refreshExpiredTimeMs));
	}
}
