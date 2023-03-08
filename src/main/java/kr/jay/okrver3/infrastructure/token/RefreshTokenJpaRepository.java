package kr.jay.okrver3.infrastructure.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.token.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUserEmailAndRefreshToken(String userEmail, String refreshToken);

	Optional<RefreshToken> findByUserEmail(String userEmail);
}
