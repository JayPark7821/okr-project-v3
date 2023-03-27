package kr.service.okr.interfaces.notification;

import org.springframework.stereotype.Component;

import kr.service.okr.domain.notification.info.NotificationInfo;
import kr.service.okr.interfaces.notification.response.NotificationResponse;

@Component
public class NotificationDtoMapper {

	NotificationResponse of(NotificationInfo info) {
		return new NotificationResponse(
			info.notiToken(),
			info.notiType().name(),
			info.msg(),
			info.status().name(),
			info.createdDate()
		);
	}

}
