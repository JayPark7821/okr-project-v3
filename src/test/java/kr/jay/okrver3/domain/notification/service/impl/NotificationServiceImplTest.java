package kr.jay.okrver3.domain.notification.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;

@DataJpaTest
@Import(NotificationServiceImpl.class)
class NotificationServiceImplTest {

	@Autowired
	private NotificationServiceImpl sut;

	@Test
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");

		Notification noti = new Notification(user, Notifications.PROJECT_FINISHED,
			Notifications.PROJECT_FINISHED.getMsg("프로젝트명"));

		sut.sendNotification(noti);

	}
}