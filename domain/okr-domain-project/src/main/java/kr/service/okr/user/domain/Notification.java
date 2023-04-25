package kr.service.okr.user.domain;

import java.time.LocalDateTime;

public record Notification(
	Long id,
	String notificationToken,
	User user,
	Long userSeq,
	String msg,
	boolean isChecked,
	boolean deleted,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate

) {
}
