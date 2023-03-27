package kr.service.okr.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.okr.domain.user.User;
import kr.service.okr.domain.user.service.UserRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
