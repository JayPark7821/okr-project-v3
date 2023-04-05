package kr.service.okr.persistence.service.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.token.RefreshTokenJpaEntity;
import kr.service.okr.persistence.repository.token.RefreshTokenJpaRepository;
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
