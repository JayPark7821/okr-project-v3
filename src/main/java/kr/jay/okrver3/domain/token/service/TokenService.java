package kr.jay.okrver3.domain.token.service;

public interface TokenService {
	AuthTokenInfo generateTokenSet(String userEmail);
}
