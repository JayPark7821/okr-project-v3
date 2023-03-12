package kr.jay.okrver3.application.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.common.io.Files;

import kr.jay.okrver3.domain.notification.info.NotificationInfo;

public class NotificationFacade {
	public Page<NotificationInfo> getNotifications(Pageable pageable, Long userFromAuthentication){
		throw new UnsupportedOperationException(
			"kr.jay.okrver3.application.notification.NotificationFacade.getNotifications()");
	}
}
