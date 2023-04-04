package kr.service.persistence.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.user.UserJpaEntity;
import kr.service.persistence.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQuery {

	private final UserJpaRepository userJpaRepository;

	public Optional<UserJpaEntity> findByEmail(String email) {
		return userJpaRepository.findByEmail(email);
	}

}
