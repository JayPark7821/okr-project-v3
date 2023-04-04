package kr.service.persistence.service.token;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.persistence.repository.token.RefreshTokenJpaRepository;
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
