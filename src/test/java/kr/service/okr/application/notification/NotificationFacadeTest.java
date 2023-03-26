package kr.service.okr.application.notification;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import kr.service.okr.domain.notification.Notification;
import kr.service.okr.domain.notification.NotificationServiceImpl;
import kr.service.okr.domain.notification.info.NotificationInfo;
import kr.service.okr.infrastructure.notification.NotificationJDBCRepository;
import kr.service.okr.infrastructure.notification.NotificationQueryDslRepository;
import kr.service.okr.infrastructure.notification.NotificationRepositoryImpl;

@DataJpaTest
@Import({NotificationFacade.class, NotificationServiceImpl.class, NotificationQueryDslRepository.class,
	NotificationJDBCRepository.class, NotificationRepositoryImpl.class})
class NotificationFacadeTest {

	@Autowired
	private NotificationFacade sut;

	@PersistenceContext
	EntityManager em;

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

}