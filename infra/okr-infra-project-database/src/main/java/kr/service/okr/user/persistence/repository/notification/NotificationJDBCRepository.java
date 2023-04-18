package kr.service.okr.user.persistence.repository.notification;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.persistence.entity.notification.NotificationJpaEntity;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationJDBCRepository {
	private static final String TABLE = "notification";
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void bulkInsert(List<NotificationJpaEntity> notifications) {

		String sql = String.format("""
				INSERT INTO `%s` (message, notification_token, user_seq, checked ,deleted)
				VALUES (:message, :notificationToken, :user, false, false)
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

