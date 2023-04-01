package kr.service.okrbatch.repository;

import kr.service.okrcommon.common.enums.Notifications;
import kr.service.okrcommon.common.utils.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationEntity {

	private static final String NOTIFICATION_PREFIX = "noti-";

	private String notificationToken;

	private Long userSeq;

	private Notifications type;

	private String msg;

	private boolean checked = false;

	private boolean deleted = false;

	@Builder
	private NotificationEntity(Long userSeq, Notifications type, String... args) {
		this.notificationToken = TokenGenerator.randomCharacterWithPrefix(NOTIFICATION_PREFIX);
		this.userSeq = userSeq;
		this.type = type;
		this.msg = type.getMsg(args);
	}

}
