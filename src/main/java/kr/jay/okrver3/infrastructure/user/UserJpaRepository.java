package kr.jay.okrver3.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
