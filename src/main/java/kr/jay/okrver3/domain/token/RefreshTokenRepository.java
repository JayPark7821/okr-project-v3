package kr.jay.okrver3.domain.token;

import java.util.Optional;

public interface RefreshTokenRepository {
	RefreshToken save(RefreshToken refreshToken);

	Optional<RefreshToken> findByUserSeq(Long userSeq);

}
