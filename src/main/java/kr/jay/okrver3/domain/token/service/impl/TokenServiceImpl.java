package kr.jay.okrver3.domain.token.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.RefreshTokenRepository;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
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

	@Override
	public AuthTokenInfo generateTokenSet(UserInfo userInfo){
		String accessToken = JwtTokenUtils.generateToken(userInfo.email(), secretKey, accessExpiredTimeMs);
		String refreshToken = JwtTokenUtils.generateToken(userInfo.email(), secretKey, refreshExpiredTimeMs);
		RefreshToken savedRefreshToken = refreshTokenRepository.save(new RefreshToken(userInfo.userSeq(),refreshToken ));

		return new AuthTokenInfo(accessToken, savedRefreshToken.getRefreshToken());
	}
}
