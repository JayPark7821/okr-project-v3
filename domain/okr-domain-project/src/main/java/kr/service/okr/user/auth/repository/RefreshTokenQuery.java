package kr.service.okr.user.auth.repository;

import java.util.Optional;

import kr.service.okr.user.auth.domain.RefreshToken;

public interface RefreshTokenQuery {
	Optional<RefreshToken> findByUserEmail(String email);
}
