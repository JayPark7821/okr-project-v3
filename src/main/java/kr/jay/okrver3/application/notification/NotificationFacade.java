package kr.jay.okrver3.application.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.notification.NotificationService;
import kr.jay.okrver3.domain.notification.info.NotificationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationFacade {

	private final NotificationService notificationService;

	public Page<NotificationInfo> getNotifications(Pageable pageable, Long userSeq) {
		return notificationService.getNotifications(pageable, userSeq);
	}
}
