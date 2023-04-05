package kr.service.okr.persistence.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.user.UserJpaEntity;
import kr.service.okr.persistence.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommand {

	private final UserJpaRepository userJpaRepository;

	public UserJpaEntity save(UserJpaEntity user) {
		return userJpaRepository.save(user);
	}
}
