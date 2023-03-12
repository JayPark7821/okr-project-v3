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

@DataJpaTest
@Import({NotificationServiceImpl.class})
class NotificationFacadeTest {

	@Autowired
	private NotificationFacade sut;

	@Test
	@Sql("classpath:insert-project-date.sql")
	void getNotifications을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		List<String> notificationTokens = List.of("noti_e144441Zey1SERx", "noti_e3eeddoZey1SERx", "noti_e2222y1SERx");
		final Page<NotificationInfo> response = sut.getNotifications(
			PageRequest.of(0, 5), 16L
		);

		assertThat(response.getTotalElements()).isEqualTo(3);
		List<NotificationInfo> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {
			NotificationInfo r = content.get(i);
			assertThat(r.notiToken()).isEqualTo(notificationTokens.get(i));

		}
	}
}