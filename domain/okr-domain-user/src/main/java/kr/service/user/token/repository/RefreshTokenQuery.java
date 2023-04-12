package kr.service.user.token.repository;

import java.util.Optional;

import kr.service.user.token.domain.RefreshToken;

public interface RefreshTokenQuery {
	Optional<RefreshToken> findByUserEmail(String email);
}
