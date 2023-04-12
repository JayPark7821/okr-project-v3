package kr.service.user.token.repository;

public interface JwtService {

	String generateJwtToken(String userEmail, boolean isRefreshToken);

	boolean needNewRefreshToken(String token);
}
