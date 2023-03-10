package kr.jay.okrver3.domain.notification.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.NotificationServiceImpl;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.notification.info.NotificationInfo;
import kr.jay.okrver3.domain.user.JobField;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.notification.NotificationJDBCRepository;
import kr.jay.okrver3.infrastructure.notification.NotificationQueryDslRepository;
import kr.jay.okrver3.infrastructure.notification.NotificationRepositoryImpl;

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
	@Sql("classpath:insert-project-date.sql")
	void getNotifications을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		List<String> notificationTokens = List.of("noti_aaaaaMoZey1SERx", "noti_e144441Zey1SERx");
		final Page<NotificationInfo> response = sut.getNotifications(
			PageRequest.of(0, 5), 16L
		);

		assertThat(response.getTotalElements()).isEqualTo(2);
		List<NotificationInfo> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {
			NotificationInfo r = content.get(i);
			assertThat(r.notiToken()).isEqualTo(notificationTokens.get(i));

		}
	}
}