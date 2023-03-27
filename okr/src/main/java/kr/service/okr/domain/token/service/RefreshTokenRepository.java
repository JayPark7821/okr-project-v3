package kr.service.okr.domain.token.service;

import java.util.Optional;

import kr.service.okr.domain.token.RefreshToken;

public interface RefreshTokenRepository {
	RefreshToken save(RefreshToken refreshToken);

	Optional<RefreshToken> findByUserEmail(String userEmail);

	Optional<RefreshToken> findByEmailAndRefreshToken(String email, String refreshToken);
}
