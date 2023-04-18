package kr.service.okr.user.persistence.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.persistence.entity.user.UserJpaEntity;
import kr.service.okr.user.persistence.repository.user.UserJpaRepository;
import kr.service.okr.user.user.domain.User;
import kr.service.okr.user.user.repository.UserQuery;
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

	@Override
	public List<Long> findUserSeqsByEmails(final List<String> emails) {
		return userJpaRepository.findUserSeqsByEmails(emails);
	}

}
