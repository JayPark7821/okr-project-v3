package kr.service.okr.user.notification.domain;

import java.time.LocalDateTime;

import kr.service.okr.user.user.domain.User;

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