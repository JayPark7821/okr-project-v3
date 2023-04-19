package kr.service.okr.user.persistence.repository.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.user.persistence.entity.token.RefreshTokenJpaEntity;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

	@Query("select r "
		+ "from RefreshTokenJpaEntity r "
		+ "where r.refreshToken = :refreshToken "
		+ "and r.userEmail = :userEmail "
	)
	Optional<RefreshTokenJpaEntity> findByUserEmailAndRefreshToken(
		@Param("userEmail") String userEmail,
		@Param("refreshToken") String refreshToken
	);

	Optional<RefreshTokenJpaEntity> findByUserEmail(String userEmail);
}
