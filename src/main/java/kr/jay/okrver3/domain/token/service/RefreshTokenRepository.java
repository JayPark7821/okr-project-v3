package kr.jay.okrver3.domain.token.service;

import java.util.Optional;

import kr.jay.okrver3.domain.token.RefreshToken;

public interface RefreshTokenRepository {
	RefreshToken save(RefreshToken refreshToken);

	Optional<RefreshToken> findByUserSeq(Long userSeq);

}
