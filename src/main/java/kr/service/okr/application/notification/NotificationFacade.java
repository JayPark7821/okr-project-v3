package kr.service.okr.application.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.service.okr.domain.notification.NotificationService;
import kr.service.okr.domain.notification.info.NotificationInfo;
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
