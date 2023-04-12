package kr.service.user.persistence.service.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.user.persistence.repository.token.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenCommand {

	private final RefreshTokenJpaRepository repository;

	public RefreshTokenJpaEntity save(RefreshTokenJpaEntity refreshToken) {
		return repository.save(refreshToken);
	}

}
