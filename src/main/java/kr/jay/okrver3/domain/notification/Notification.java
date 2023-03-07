package kr.jay.okrver3.domain.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.jay.okrver3.common.utils.TokenGenerator;
import kr.jay.okrver3.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class Notification {

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

	public Notification(Long userSeq, Notifications type, String... args) {
		this.notificationToken = TokenGenerator.randomCharacterWithPrefix(NOTIFICATION_PREFIX);
		this.userSeq = userSeq;
		this.type = type;
		this.msg = type.getMsg(args);
	}
}
