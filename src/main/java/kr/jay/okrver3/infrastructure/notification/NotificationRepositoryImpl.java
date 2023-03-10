package kr.jay.okrver3.infrastructure.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

	private final NotificationJDBCRepository notificationJDBCRepository;
	private final NotificationJpaRepository notificationJpaRepository;
	private final NotificationQueryDslRepository NotificationQueryDslRepository;

	@Override
	public void bulkInsert(List<Notification> notificationStream) {
		notificationJDBCRepository.bulkInsert(notificationStream);
	}

	@Override
	public Notification save(Notification notification) {
		return notificationJpaRepository.save(notification);
	}

	@Override
	public Page<Notification> findNotificationByUserSeq(Pageable pageable, Long userSeq) {
		return NotificationQueryDslRepository.findNotificationByUserSeq(pageable, userSeq);
	}
}
