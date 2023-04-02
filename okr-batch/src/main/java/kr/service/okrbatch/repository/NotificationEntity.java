package kr.service.okrbatch.repository;

import java.time.LocalDateTime;

import kr.service.okrcommon.common.enums.Notifications;
import kr.service.okrcommon.common.utils.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationEntity {

	private static final String NOTIFICATION_PREFIX = "noti-";

	private final String notificationToken;

	private final Long userSeq;
	private final String msg;

	private final boolean checked = false;

	private final boolean deleted = false;
	private final String createdBy;
	private final String createdDate;
	private final String lastModifiedBy;
	private final String lastModifiedDate;

	@Builder
	private NotificationEntity(Long userSeq, String projectName) {
		this.notificationToken = TokenGenerator.randomCharacterWithPrefix(NOTIFICATION_PREFIX);
		this.userSeq = userSeq;
		this.msg = Notifications.PROJECT_FINISHED.getMsg(projectName);
		this.createdBy = "SYSTEM";
		this.lastModifiedBy = "SYSTEM";
		this.createdDate = LocalDateTime.now().toString();
		this.lastModifiedDate = LocalDateTime.now().toString();
	}

}
