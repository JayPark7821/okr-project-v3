package kr.jay.okrver3.infrastructure.notification;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.domain.notification.Notification;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationJDBCRepository {
	private static final String TABLE = "notification";
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void bulkInsert(List<Notification> notifications) {

		String sql = String.format("""
				INSERT INTO `%s` (message, notification_token, user_seq)
				VALUES (:message, :notificationToken, :user )
			""", TABLE);

		SqlParameterSource[] params = notifications.stream()
			.map(noti -> new MapSqlParameterSource()
				.addValue("message", noti.getMsg())
				.addValue("notificationToken", noti.getNotificationToken())
				.addValue("user", noti.getUserSeq()))
			.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);
	}

}

