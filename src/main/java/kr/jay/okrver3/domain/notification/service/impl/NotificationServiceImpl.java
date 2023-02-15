package kr.jay.okrver3.domain.notification.service.impl;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	public void sendNotification(Notification notification) {
		notificationRepository.save(notification);
	}
}
