package kr.service.okr.domain.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {
	void bulkInsert(List<Notification> notificationStream);

	Notification save(Notification notification);

	Page<Notification> findNotificationByUserSeq(Pageable pageable, Long userSeq);

	Optional<Notification> findByNotificationTokenAndUserSeq(String notificationToken, Long userSeq);

	void delete(Notification notification);
}
