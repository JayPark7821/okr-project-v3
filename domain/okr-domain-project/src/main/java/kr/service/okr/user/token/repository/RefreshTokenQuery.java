package kr.service.okr.user.token.repository;

import java.util.Optional;

import kr.service.okr.user.token.domain.RefreshToken;

public interface RefreshTokenQuery {
	Optional<RefreshToken> findByUserEmail(String email);
}
