package kr.jay.okrver3.interfaces.notification;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.notification.info.NotificationInfo;
import kr.jay.okrver3.interfaces.notification.response.NotificationResponse;

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
