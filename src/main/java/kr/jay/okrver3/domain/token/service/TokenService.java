package kr.jay.okrver3.domain.token.service;

import kr.jay.okrver3.domain.user.info.UserInfo;

public interface TokenService {
	AuthTokenInfo generateTokenSet( UserInfo userInfo);
	AuthTokenInfo getNewAccessToken(String accessToken);
}
