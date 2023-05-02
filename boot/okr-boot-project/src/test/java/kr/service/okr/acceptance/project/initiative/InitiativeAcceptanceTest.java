package kr.service.okr.acceptance.project.initiative;

import static kr.service.okr.acceptance.project.initiative.InitiativeAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.initiative.InitiativeAcceptanceTestSteps.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okr.project.api.RegisterInitiativeRequest;
import kr.service.okr.user.domain.AuthenticationProvider;
import kr.service.okr.utils.SpringBootTestReady;
import kr.service.okr.utils.TestHelpUtils;

@DisplayName("Initiative 도메인 인수 테스트")
public class InitiativeAcceptanceTest extends SpringBootTestReady {

	String 사용자_토큰;

	@BeforeEach
	void beforeEach() throws IOException {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
		사용자_토큰 = AuthenticationProvider.generateAccessToken("projectMasterTest@naver.com");
	}

	@Test
	@DisplayName("행동전략 추가시 기대하는 응답(initiativeToken)을 반환한다.")
	void add_initiative() throws Exception {
		//given
		var 행동전략_생성_데이터 = 행동전략_저장_요청_데이터_생성("행동전략1", "key_wV6MX15WQ3DTzQMs", "행동전략 상세");

		//when
		var 응답 = 횅동전략_추가_요청(행동전략_생성_데이터, 사용자_토큰);

		//then
		행동전략_추가_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("행동전략 추가시 진척도가 변경된다 (동시성 테스트)")
	void project_progress_will_change_when_initiative_added_concurrency_test() throws Exception {
		//given
		var 행동전략_생성_데이터 = 행동전략_저장_요청_데이터_생성("행동전략1", "key_wV6MX15WQ3DTzQMs", "행동전략 상세");

		//when
		행동전략_추가_요청_99건_동시성_테스트(행동전략_생성_데이터, 사용자_토큰);

		//then
		행동전략_추가_요청_동시성_테스트_검증(em);
	}

	private RegisterInitiativeRequest 행동전략_저장_요청_데이터_생성(String 행동전략명, String 핵심결과_토큰, String 행동전략_상세_내용) {
		return new RegisterInitiativeRequest(
			핵심결과_토큰,
			행동전략명,
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-10, "yyyy-MM-dd"),
			행동전략_상세_내용
		);
	}
}
