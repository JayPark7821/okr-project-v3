package kr.service.okr.persistence.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.okr.persistence.entity.user.UserJpaEntity;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	Optional<UserJpaEntity> findByEmail(String email);
}
