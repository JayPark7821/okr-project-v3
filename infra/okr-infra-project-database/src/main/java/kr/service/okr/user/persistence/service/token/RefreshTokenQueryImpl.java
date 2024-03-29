package kr.service.okr.user.persistence.service.token;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.okr.user.persistence.repository.token.RefreshTokenJpaRepository;
import kr.service.okr.user.repository.token.RefreshTokenQuery;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class RefreshTokenQueryImpl implements RefreshTokenQuery {

	private final RefreshTokenJpaRepository repository;

	@Override
	public Optional<RefreshToken> findByUserEmail(final String email) {
		return repository.findByUserEmail(email).map(RefreshTokenJpaEntity::toDomain);
	}
}
