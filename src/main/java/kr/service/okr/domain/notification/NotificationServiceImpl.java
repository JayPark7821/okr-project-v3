package kr.service.okr.domain.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.service.okr.domain.notification.info.NotificationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	@Override
	public void sendNotification(
		Notifications notificationType,
		List<Long> notificationReceiveUserSeq,
		String... args
	) {
		notificationRepository.bulkInsert(notificationReceiveUserSeq.stream()
			.map(user -> new Notification(user, notificationType, args)).toList());
	}

	@Override
	public void sendNotification(Notifications notificationType, Long userSeq, String... args) {
		notificationRepository.save(new Notification(userSeq, notificationType, args));
	}

	@Override
	public Page<NotificationInfo> getNotifications(Pageable pageable, Long userSeq) {
		return notificationRepository.findNotificationByUserSeq(pageable, userSeq)
			.map(NotificationInfo::new);
	}
}
