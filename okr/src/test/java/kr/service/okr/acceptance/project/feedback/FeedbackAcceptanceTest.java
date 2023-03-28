package kr.service.okr.acceptance.project.feedback;

import static kr.service.okr.acceptance.project.feedback.FeedbackAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.feedback.FeedbackAcceptanceTestSteps.*;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okrcommon.common.utils.JwtTokenUtils;
import kr.service.okr.util.SpringBootTestReady;

@DisplayName("Feedback(피드백) 도메인 인수 테스트")
public class FeedbackAcceptanceTest extends SpringBootTestReady {

	@PersistenceContext
	EntityManager em;
	String 사용자_토큰;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
		사용자_토큰 = JwtTokenUtils.generateToken("projectMasterTest@naver.com", key, 엑세스_토큰_유효기간_임계값);
	}

	@Test
	@DisplayName("팀원의 행동전략에 피드백을 추가하면 기대하는 응답을 반환한다")
	void add_feedback_to_initiative() throws Exception {
		//given
		var 행동전략_토큰 = "ini_ixYjj5nODqtb3AH8";
		var 피드백_내용 = "피드백!!!!!!";
		var 피드백_이모티콘 = "GOOD_IDEA";

		//when
		var 응답 = 피드백_추가_요청(행동전략_토큰, 피드백_내용, 피드백_이모티콘, 사용자_토큰);

		//then
		핵심결과_추가_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("행동전략토큰으로 피드백을 요청 하면 기대하는 응답를 반환 한다")
	void get_feedbacks_by_initiativeToken() throws Exception {
		//given
		var 행동전략_토큰 = "ini_ixYjj5nODqtb3AH8";

		//when
		var 응답 = 행동전략에_등록된_피드백_조회_요청(행동전략_토큰, 사용자_토큰);

		//then
		피드백_조회_요청_검증(응답);
	}

	@Test
	@DisplayName("팀원의 완료된 행동전략중 아직 피드백을 남기지않은 행동전략의 count를 반환한다")
	void get_initiative_counts_have_not_give_feedback_yet() throws Exception {
		//given
		var 피드백_사용자_토큰 = JwtTokenUtils.generateToken("user1@naver.com", key, 엑세스_토큰_유효기간_임계값);

		//when
		var 응답 = 피드백_남기지_않은_행동전략_카운트_조회_요청(피드백_사용자_토큰);

		//then
		피드백_남기지_않은_행동전략_카운트_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("받은 피드백을 조회 요청하면 기대하는 응답을 반환한다")
	void get_received_feedbacks() throws Exception {
		//given
		var 검색범위 = "ALL";
		var 피드백_조회요청_사용자_토큰 = JwtTokenUtils.generateToken("user1@naver.com", key, 엑세스_토큰_유효기간_임계값);

		//when
		var 응답 = 받은_피드백_조회_요청(검색범위, 피드백_조회요청_사용자_토큰);

		//then
		받은_피드백_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("피드백 상태 업데이트 요청을 하면 기대하는 응답을 반환한다")
	void update_ㄹeedback_status() throws Exception {
		//given
		var 피드백_토큰 = "feedback_el6q34zazzSyWx9";

		//when
		var 응답 = 피드백_상태_업데이트_요청(피드백_토큰, 사용자_토큰);

		//then
		// 피드백_상태_업데이트_응답_검증(응답);
	}

	@Test
	@DisplayName("피드백을 남겨야 하는 행동전략 정보를 요청하면 기대하는 응답을 리턴한다.")
	void get_initiative_info_for_feedback() throws Exception {
		//given
		var 피드백_행동전략_조회_사용자_인증_토큰 = JwtTokenUtils.generateToken("user1@naver.com", key, 엑세스_토큰_유효기간_임계값);
		//when
		var 응답 = 피드백을_남겨야_하는_행동전략_조회_요청(피드백_행동전략_조회_사용자_인증_토큰);

		//then
		피드백을_남겨야_하는_행동전략_응답_검증(응답);
	}

}
