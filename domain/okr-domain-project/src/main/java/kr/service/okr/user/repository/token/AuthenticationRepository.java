package kr.service.okr.user.repository.token;

import java.util.Optional;

public interface AuthenticationRepository {
	Optional<String> findEmailByToken(final String token);

	boolean isTokenExpired(final String token);

	String generateRefreshToken(final String email);

	String generateAccessToken(final String email);
}
