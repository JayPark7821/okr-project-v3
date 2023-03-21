package kr.service.okr.domain.user.service;

import java.util.Optional;

import kr.service.okr.domain.user.User;

public interface UserRepository {
	Optional<User> findByEmail(String email);
	User getReferenceById(Long id);
	User save(User user);
	Optional<User> findById(Long userSeq);
}
