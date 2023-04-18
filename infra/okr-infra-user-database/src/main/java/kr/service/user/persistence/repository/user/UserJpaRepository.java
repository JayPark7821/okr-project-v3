package kr.service.user.persistence.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.service.user.persistence.entity.user.UserJpaEntity;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	Optional<UserJpaEntity> findByEmail(String email);

	@Query("select u.userSeq from UserJpaEntity u where u.email in :emails")
	List<Long> findUserSeqsByEmails(List<String> emails);
}
