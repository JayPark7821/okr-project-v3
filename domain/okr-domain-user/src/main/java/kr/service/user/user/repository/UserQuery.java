package kr.service.user.user.repository;

import java.util.Optional;

import kr.service.user.user.domain.User;

public interface UserQuery {

	Optional<User> findByEmail(String email);
}
