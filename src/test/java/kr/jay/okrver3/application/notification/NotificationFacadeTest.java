package kr.jay.okrver3.application.notification;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.notification.NotificationServiceImpl;
import kr.jay.okrver3.domain.notification.info.NotificationInfo;
import kr.jay.okrver3.infrastructure.notification.NotificationJDBCRepository;
import kr.jay.okrver3.infrastructure.notification.NotificationQueryDslRepository;
import kr.jay.okrver3.infrastructure.notification.NotificationRepositoryImpl;

@DataJpaTest
@Import({NotificationFacade.class, NotificationServiceImpl.class, NotificationQueryDslRepository.class,
	NotificationJDBCRepository.class, NotificationRepositoryImpl.class})
class NotificationFacadeTest {

	@Autowired
	private NotificationFacade sut;

	@Test
	@Sql("classpath:insert-project-data.sql")
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