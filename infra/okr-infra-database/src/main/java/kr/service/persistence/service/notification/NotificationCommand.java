package kr.service.persistence.service.notification;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.notification.NotificationJpaEntity;
import kr.service.persistence.repository.notification.NotificationJDBCRepository;
import kr.service.persistence.repository.notification.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommand {

	private final NotificationJDBCRepository notificationJDBCRepository;
	private final NotificationJpaRepository notificationJpaRepository;

	public void bulkInsert(List<NotificationJpaEntity> notificationStream) {
		notificationJDBCRepository.bulkInsert(notificationStream);
	}

	public NotificationJpaEntity save(NotificationJpaEntity notification) {
		return notificationJpaRepository.save(notification);
	}

	public void delete(final NotificationJpaEntity notification) {
		notificationJpaRepository.delete(notification);
	}

}
