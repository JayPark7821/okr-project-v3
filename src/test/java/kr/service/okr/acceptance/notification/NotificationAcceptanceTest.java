package kr.service.okr.acceptance.notification;

import static kr.service.okr.acceptance.notification.NotificationAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.notification.NotificationAcceptanceTestSteps.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okr.common.utils.JwtTokenUtils;
import kr.service.okr.util.SpringBootTestReady;

@DisplayName("Notification(알림) 도메인 인수 테스트")
public class NotificationAcceptanceTest extends SpringBootTestReady {

	@PersistenceContext
	EntityManager em;
	String 사용자_토큰;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
		사용자_토큰 = JwtTokenUtils.generateToken("notificationTest@naver.com", key, 엑세스_토큰_유효기간_임계값);
	}

	@Test
	@DisplayName("알림을 요청하면 기대하는 응답을 반환한다")
	void get_notifications_list() throws Exception {
		//when
		var 응답 = 알림_조회_요청(사용자_토큰);

		//then
		알림_조회_요청_응답_검증(응답);
	}
}

