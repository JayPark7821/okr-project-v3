package kr.service.okr.persistence.repository.notification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.persistence.entity.notification.NotificationJpaEntity;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {

	@Query("select n "
		+ "from NotificationJpaEntity n "
		+ "where n.notificationToken = :notificationToken "
		+ "AND n.userSeq = :userSeq")
	Optional<NotificationJpaEntity> findByNotificationTokenAndUserSeq(
		@Param("notificationToken") String notificationToken,
		@Param("userSeq") Long userSeq
	);
}
