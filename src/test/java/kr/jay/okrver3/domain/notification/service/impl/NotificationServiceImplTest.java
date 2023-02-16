package kr.jay.okrver3.domain.notification.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.notification.NotificationJDBCRepository;

@DataJpaTest
@Import({NotificationServiceImpl.class, NotificationJDBCRepository.class})
class NotificationServiceImplTest {

	@Autowired
	private NotificationServiceImpl sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql({"classpath:insert-user.sql"})
	@DisplayName("notification저장에 성공")
	void send_notification() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");

		sut.sendInvitationNotification(List.of(user),"프로젝트명" , "invitedUser");

		List<Notification> result = em.createQuery("select n from Notification n", Notification.class)
			.getResultList();
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getMsg()).isEqualTo(Notifications.NEW_TEAM_MATE.getMsg("invitedUser", "프로젝트명"));
	}
}