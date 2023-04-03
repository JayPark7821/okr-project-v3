package kr.service.okr.acceptance.notification;

import static kr.service.okr.acceptance.notification.NotificationAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.notification.NotificationAcceptanceTestSteps.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

	@Test
	@DisplayName("알림을 확인하면 기대하는 응답을 반환한다")
	void check_notification() throws Exception {
		//given
		var 확인_요청_알림_토큰 = "noti_e2222y1SERx";
		//when
		var 응답 = 알림_확인_요청(확인_요청_알림_토큰, 사용자_토큰);
		//then
		알림_확인_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("알림을 삭제하면 기대하는 응답을 반환한다")
	void delete_notification() throws Exception {
		//given
		var 삭제_요청_알림_토큰 = "noti_e2222y1SERx";
		//when
		var 응답 = 알림_삭제_요청(삭제_요청_알림_토큰, 사용자_토큰);
		//then
		알림_삭제_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("다른 사용자의 알림 삭제를 요청하면 기대하는 응답을 반환한다")
	void throw_exception_when_delete_other_users_notification() throws Exception {
		//given
		var 삭제_요청_알림_토큰 = "noti_111fey1SERx";
		//when
		var 응답 = 알림_삭제_요청(삭제_요청_알림_토큰, 사용자_토큰);
		//then
		알림_삭제_요청_응답_검증_실패(응답);
	}

}

