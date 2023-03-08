package kr.jay.okrver3.infrastructure.token;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.service.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{

	private final RefreshTokenJpaRepository repository;
	@Override
	public RefreshToken save(RefreshToken refreshToken) {
		return repository.save(refreshToken);
	}

	@Override
	public Optional<RefreshToken> findByUserEmail(String userEmail) {
		return repository.findByUserEmail(userEmail);
	}

	@Override
	public Optional<RefreshToken> findByEmailAndRefreshToken(String email, String refreshToken) {
		return repository.findByUserEmailAndRefreshToken(email, refreshToken);
	}
}
