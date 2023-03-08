package kr.jay.okrver3.infrastructure.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.token.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

	@Query("select r "
		+ "from RefreshToken r "
		+ "where r.refreshToken = :refreshToken "
		+ "and r.userEmail = :userEmail "
	)
	Optional<RefreshToken> findByUserEmailAndRefreshToken(
		@Param("userEmail")String userEmail,
		@Param("refreshToken")String refreshToken
	);

	Optional<RefreshToken> findByUserEmail(String userEmail);
}
