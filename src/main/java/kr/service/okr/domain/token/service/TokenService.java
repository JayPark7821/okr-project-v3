package kr.service.okr.domain.token.service;

import kr.service.okr.domain.user.info.UserInfo;

public interface TokenService {
	AuthTokenInfo generateTokenSet( UserInfo userInfo);
	AuthTokenInfo getNewAccessToken(String refreshToken);
}
