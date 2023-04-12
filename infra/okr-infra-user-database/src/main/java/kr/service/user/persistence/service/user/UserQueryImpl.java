package kr.service.user.persistence.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.persistence.entity.user.UserJpaEntity;
import kr.service.user.persistence.repository.user.UserJpaRepository;
import kr.service.user.user.domain.User;
import kr.service.user.user.repository.UserQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryImpl implements UserQuery {

	private final UserJpaRepository userJpaRepository;

	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findByEmail(email)
			.map(UserJpaEntity::toDomain);
	}

}
