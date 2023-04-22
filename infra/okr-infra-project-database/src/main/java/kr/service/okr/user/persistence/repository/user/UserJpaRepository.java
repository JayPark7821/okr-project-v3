package kr.service.okr.user.persistence.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.user.persistence.entity.user.UserJpaEntity;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	Optional<UserJpaEntity> findByEmail(String email);

	@Query("select u.userSeq from UserJpaEntity u where u.email in :emails")
	List<Long> findUserSeqsByEmails(@Param("emails") List<String> emails);
}
