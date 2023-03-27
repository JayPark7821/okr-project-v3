package kr.service.okr.domain.notification;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.service.okr.common.audit.BaseEntity;
import kr.service.okr.common.utils.TokenGenerator;
import kr.service.okr.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE notification SET deleted = true WHERE notification_id = ?")
@Where(clause = "deleted = false")
@Table(name = "notification")
public class Notification extends BaseEntity {

	private static final String NOTIFICATION_PREFIX = "noti-";

	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String notificationToken;
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", insertable = false, updatable = false)
	private User user;

	@Column(name = "user_seq")
	private Long userSeq;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private Notifications type;

	@Column(name = "message")
	private String msg;

	@Column(name = "checked")
	private boolean isChecked = false;

	@Column(nullable = false)
	private boolean deleted = false;

	public Notification(Long userSeq, Notifications type, String... args) {
		this.notificationToken = TokenGenerator.randomCharacterWithPrefix(NOTIFICATION_PREFIX);
		this.userSeq = userSeq;
		this.type = type;
		this.msg = type.getMsg(args);
	}

	public void checkNotification() {
		this.isChecked = true;
	}
}
