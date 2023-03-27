package kr.service.okr.domain.notification.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import kr.service.okr.domain.notification.Notification;
import kr.service.okr.domain.notification.NotificationServiceImpl;
import kr.service.okr.domain.notification.Notifications;
import kr.service.okr.domain.notification.info.NotificationInfo;
import kr.service.okr.domain.user.JobField;
import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.domain.user.RoleType;
import kr.service.okr.domain.user.User;
import kr.service.okr.infrastructure.notification.NotificationJDBCRepository;
import kr.service.okr.infrastructure.notification.NotificationQueryDslRepository;
import kr.service.okr.infrastructure.notification.NotificationRepositoryImpl;

@DataJpaTest
@Import({NotificationServiceImpl.class, NotificationJDBCRepository.class, NotificationRepositoryImpl.class
	, NotificationQueryDslRepository.class})
class NotificationServiceImplTest {

	@Autowired
	private NotificationServiceImpl sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql({"classpath:insert-user.sql"})
	@DisplayName("notification저장에 성공")
	void send_notification() throws Exception {

		User user = new User(999L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobField.WEB_FRONT_END_DEVELOPER);

		sut.sendNotification(Notifications.NEW_TEAM_MATE, List.of(user.getUserSeq()), "invitedUser", "프로젝트명");

		List<Notification> result = em.createQuery("select n from Notification n", Notification.class)
			.getResultList();
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getMsg()).isEqualTo(Notifications.NEW_TEAM_MATE.getMsg("invitedUser", "프로젝트명"));
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void getNotifications을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		List<String> notificationTokens = List.of("noti_e144441Zey1SERx", "noti_e3eeddoZey1SERx",
			"noti_aaaaaMoZey1SERx", "noti_e2222y1SERx");
		final Page<NotificationInfo> response = sut.getNotifications(
			PageRequest.of(0, 5), 16L
		);

		assertThat(response.getTotalElements()).isEqualTo(4);
		List<NotificationInfo> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {
			NotificationInfo r = content.get(i);
			assertThat(r.notiToken()).isEqualTo(notificationTokens.get(i));

		}
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void checkNotification을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		String notificationToken = "noti_111fey1SERx";

		sut.checkNotification(notificationToken, 3L);

		final Notification notification = em.createQuery(
				"select n from Notification n where n.notificationToken = :token",
				Notification.class)
			.setParameter("token", notificationToken)
			.getSingleResult();

		assertThat(notification.isChecked()).isTrue();
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void deleteNotification을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		String notificationToken = "noti_111fey1SERx";

		sut.checkNotification(notificationToken, 3L);
		final Long notificationCount = em.createQuery(
				"select count(n) from Notification n where n.notificationToken = :token and n.deleted = true",
				Long.class)
			.setParameter("token", notificationToken)
			.getSingleResult();

		assertThat(notificationCount).isEqualTo(0L);
	}
}