package kr.service.okr.persistence.service.token;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.okr.persistence.repository.token.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenQuery {

	private final RefreshTokenJpaRepository repository;

	public Optional<RefreshTokenJpaEntity> findByUserEmail(String userEmail) {
		return repository.findByUserEmail(userEmail);
	}

	public Optional<RefreshTokenJpaEntity> findByEmailAndRefreshToken(String email, String refreshToken) {
		return repository.findByUserEmailAndRefreshToken(email, refreshToken);
	}
}