package kr.service.okr.acceptance.project.keyresult;

import static kr.service.okr.acceptance.project.keyresult.KeyResultAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.keyresult.KeyResultAcceptanceTestSteps.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okr.user.domain.AuthenticationProvider;
import kr.service.okr.utils.SpringBootTestReady;

@DisplayName("KeyResult 도메인 인수 테스트")
public class KeyResultAcceptanceTest extends SpringBootTestReady {

	String 사용자_토큰;

	@BeforeEach
	void beforeEach() throws IOException {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
		사용자_토큰 = AuthenticationProvider.generateAccessToken("projectMasterTest@naver.com");
	}

	@Test
	@DisplayName("핵심결과 추가 요청시 기대하는 응답(핵심결과 토큰)을 반환한다.")
	void add_keyResult_to_project() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_as3fg34tgg6421";

		//when
		var 응답 = 핵심결과_추가_요청(프로젝트_토큰, "핵심결과1", 사용자_토큰);

		//then
		핵심결과_추가_요청_응답_검증(응답);
	}
}
