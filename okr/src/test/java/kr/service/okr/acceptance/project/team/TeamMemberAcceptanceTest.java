package kr.service.okr.acceptance.project.team;

import static kr.service.okr.acceptance.project.team.TeamMemberAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.team.TeamMemberAcceptanceTestSteps.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.util.SpringBootTestReady;
import kr.service.okrcommon.common.utils.JwtTokenUtils;

@DisplayName("TeamMember(팀) 도메인 인수 테스트")
public class TeamMemberAcceptanceTest extends SpringBootTestReady {

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
	@DisplayName("프로젝트에 팀원 추가를 위해 email을 입력하면 추가 가능 여부을 반환한다.")
	void validate_email_address() throws Exception {
		//given
		var 초대할_팀원_이메일 = "user3@naver.com";
		var 초대할_프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 팀원_초대_가능여부_확인_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_가능여부_응답_검증(응답, 초대할_팀원_이메일);
	}

	@Test
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email검증을 요청하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {
		//given
		var 초대할_팀원_이메일 = "user3@naver.com";
		var 초대할_프로젝트_토큰 = "mst_Kiwqnp1Nq6lb6421";

		//when
		var 응답 = 팀원_초대_가능여부_확인_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_가능여부_실패_응답_검증_참여중인_프로젝트_X(응답);
	}

	@Test
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email검증을 요청하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		//given
		var 초대할_팀원_이메일 = "user3@naver.com";
		var 초대할_프로젝트_토큰 = "mst_qq2f4gbfffffe421";

		//when
		var 응답 = 팀원_초대_가능여부_확인_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_가능여부_실패_응답_검증_프로젝트_리더_X(응답);
	}

	@Test
	@DisplayName("팀원 추가를 위해 가입하지 않은 email검증을 요청하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {
		//given
		var 초대할_팀원_이메일 = "noUserEmail@naver.com";
		var 초대할_프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 팀원_초대_가능여부_확인_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_가능여부_실패_응답_검증_가입한_유저_X(응답);
	}

	@Test
	@DisplayName("이미 팀에 초대된 팀원의 email검증을 요청하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		//given
		var 초대할_팀원_이메일 = "user1@naver.com";
		var 초대할_프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 팀원_초대_가능여부_확인_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_가능여부_실패_응답_검증_이미_프로젝트에_등록된_유저(응답);
	}

	@Test
	@DisplayName("로그인된 유저 자신의 email검증을 요청하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		//given
		var 초대할_팀원_이메일 = "projectMasterTest@naver.com";
		var 초대할_프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 팀원_초대_가능여부_확인_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_가능여부_실패_응답_검증_검증_요청자의_이메일(응답);
	}

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {
		//given
		String 초대할_팀원_이메일 = "user3@naver.com";
		String 초대할_프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 팀원_초대_요청(초대할_프로젝트_토큰, 초대할_팀원_이메일, 사용자_토큰);

		//then
		팀원_초대_응답_검증(응답, 초대할_팀원_이메일);
	}
}
