package kr.jay.okrver3.domain.token.service.impl;

import static kr.jay.okrver3.common.utils.JwtTokenUtils.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

		RefreshToken refreshToken = refreshTokenRepository.findByUserSeq(userInfo.userSeq())
			.orElseGet(() -> refreshTokenRepository.save(createNewRefreshToken(userInfo)));

		if (getRemainingTimeOf(refreshToken) <= refreshTokenRegenerationThreshold)
			refreshToken.updateRefreshToken(createNewToken(userInfo, refreshExpiredTimeMs));

		return new AuthTokenInfo(createNewToken(userInfo, accessExpiredTimeMs), refreshToken.getRefreshToken());
	}

	@Override
	public AuthTokenInfo getNewAccessToken(String accessToken) {
		return null;
	}

	private long getRemainingTimeOf(RefreshToken refreshToken) {
		return getExpiration(refreshToken.getRefreshToken(), secretKey).getTime() - new Date().getTime();
	}

	private String createNewToken(UserInfo userInfo, Long expiredTimeMs) {
		return generateToken(userInfo.email(), secretKey, expiredTimeMs);
	}

	private RefreshToken createNewRefreshToken(UserInfo userInfo) {
		return new RefreshToken(userInfo.userSeq(), createNewToken(userInfo, refreshExpiredTimeMs));
	}
}
