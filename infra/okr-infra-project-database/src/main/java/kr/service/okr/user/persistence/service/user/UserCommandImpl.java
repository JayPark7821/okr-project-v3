package kr.service.okr.user.persistence.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.persistence.entity.user.UserJpaEntity;
import kr.service.okr.user.persistence.repository.user.UserJpaRepository;
import kr.service.okr.user.user.domain.User;
import kr.service.okr.user.user.repository.UserCommand;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandImpl implements UserCommand {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(final User user) {
		return userJpaRepository.save(new UserJpaEntity(user))
			.toDomain();
	}
}
