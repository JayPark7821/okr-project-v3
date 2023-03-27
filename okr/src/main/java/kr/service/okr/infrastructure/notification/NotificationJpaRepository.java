package kr.service.okr.infrastructure.notification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.domain.notification.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

	@Query("select n "
		+ "from Notification n "
		+ "where n.notificationToken = :notificationToken "
		+ "AND n.userSeq = :userSeq")
	Optional<Notification> findByNotificationTokenAndUserSeq(
		@Param("notificationToken") String notificationToken,
		@Param("userSeq") Long userSeq
	);
}
