package kr.service.user.persistence.service.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.user.persistence.repository.token.RefreshTokenJpaRepository;
import kr.service.user.token.domain.RefreshToken;
import kr.service.user.token.repository.RefreshTokenCommand;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenCommandImpl implements RefreshTokenCommand {

	private final RefreshTokenJpaRepository repository;

	@Override
	public RefreshToken save(final RefreshToken refreshToken) {
		return repository.save(new RefreshTokenJpaEntity(refreshToken)).toDomain();
	}
}
