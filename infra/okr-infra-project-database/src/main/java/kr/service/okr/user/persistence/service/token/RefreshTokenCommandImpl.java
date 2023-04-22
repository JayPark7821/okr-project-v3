package kr.service.okr.user.persistence.service.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.auth.domain.RefreshToken;
import kr.service.okr.user.auth.repository.RefreshTokenCommand;
import kr.service.okr.user.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.okr.user.persistence.repository.token.RefreshTokenJpaRepository;
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