package kr.service.okr.user.token.repository;

public interface AuthService {
	boolean needNewAuthentication(String token);

	String generateAuthToken(String email, boolean isRefreshToken);
}
