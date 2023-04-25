package kr.service.okr.user.repository.token;

import java.util.Optional;

import kr.service.okr.user.domain.RefreshToken;

public interface RefreshTokenQuery {
	Optional<RefreshToken> findByUserEmail(String email);
}
