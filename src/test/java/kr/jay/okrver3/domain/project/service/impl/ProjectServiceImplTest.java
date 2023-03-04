package kr.jay.okrver3.domain.project.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.application.project.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectTeamMemberInfo;
import kr.jay.okrver3.domain.project.validator.ProjectInitiativeDateValidator;
import kr.jay.okrver3.domain.project.validator.ProjectKeyResultCountValidator;
import kr.jay.okrver3.domain.project.validator.ProjectLeaderValidator;
import kr.jay.okrver3.domain.project.validator.ProjectPeriodValidator;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessor;
import kr.jay.okrver3.domain.user.JobFieldDetail;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.project.ProjectQueryDslRepository;
import kr.jay.okrver3.infrastructure.project.ProjectRepositoryImpl;
import kr.jay.okrver3.interfaces.project.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.interfaces.project.ProjectKeyResultSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectSideMenuResponse;

@DataJpaTest
@Import({ProjectServiceImpl.class, ProjectRepositoryImpl.class, ProjectQueryDslRepository.class, ProjectValidateProcessor.class, ProjectLeaderValidator.class,
	ProjectKeyResultCountValidator.class, ProjectPeriodValidator.class, ProjectInitiativeDateValidator.class
})
class ProjectServiceImplTest {

	@Autowired
	private ProjectServiceImpl sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {
		User user = getUser(1L);

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		ProjectInfo projectInfo = sut.registerProject(
			new ProjectMasterSaveDto("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), null), user, List.of());

		assertThat(projectInfo.projectToken()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
		assertThat(projectInfo.keyResultInfos().get(0).name()).isEqualTo("keyResult1");
		assertThat(projectInfo.keyResultInfos().get(1).name()).isEqualTo("keyResult2");
		assertThat(projectInfo.projectType()).isEqualTo(ProjectType.SINGLE.name());
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원을 추가해 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {
		User user = getUser(1L);

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		ProjectInfo projectInfo = sut.registerProject(
			new ProjectMasterSaveDto("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), user, List.of(
				new User(4L, "testId", "guest", "guest@email.com", "pic", ProviderType.GOOGLE, RoleType.USER, null,
					JobFieldDetail.WEB_SERVER_DEVELOPER)));

		assertThat(projectInfo.projectToken()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
		assertThat(projectInfo.keyResultInfos().get(0).name()).isEqualTo("keyResult1");
		assertThat(projectInfo.keyResultInfos().get(1).name()).isEqualTo("keyResult2");
		assertThat(projectInfo.projectType()).isEqualTo(ProjectType.TEAM.name());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		User user = getUser(1L);

		ProjectInfo projectInfo = sut.getProjectInfoBy("project-fgFHxGWeIUQt", user);

		assertThat(projectInfo.projectToken()).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(projectInfo.objective()).isEqualTo("projectObjective");
		assertThat(projectInfo.startDate()).isEqualTo("2020-12-01");
		assertThat(projectInfo.endDate()).isEqualTo("3999-12-12");
		assertThat(projectInfo.projectType()).isEqualTo("SINGLE");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {
		User inviter = getUser(1L);

		User invitedUser = getUser(2L);

		ProjectTeamMemberInfo response = sut.inviteTeamMember("project-fgFHxGWeIUQt", invitedUser, inviter);

		assertThat(response.projectTeamMemberUsers().size()).isEqualTo(3);

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		User user = getUser(1L);

		sut.validateUserToInvite("project-fgFHxGWeIUQt", memberEmail, user);

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		User user = getUser(2L);

		assertThatThrownBy(() -> sut.validateUserToInvite("project-fgFHxGWeIUQt", memberEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		User user = getUser(3L);

		assertThatThrownBy(() -> sut.validateUserToInvite("project-fgFHxGWeIUQt", memberEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";

		User user = getUser(1L);

		assertThatThrownBy(() -> sut.validateUserToInvite("project-fgFHxGWeIUQt", teamMemberEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_미포함_팀프로젝트() throws Exception {

		List<String> recentlyCreatedSortProject = List.of("mst_3gbyy554frgg6421", "mst_K4232g4g5rgg6421");
		User user = getUser(13L);

		Page<ProjectDetailInfo> result = sut.getDetailProjectList(
			new ProjectDetailRetrieveCommand(SortType.RECENTLY_CREATE, ProjectType.TEAM, "N", user,
				PageRequest.of(0, 5)));

		assertThat(result.getTotalElements()).isEqualTo(2);
		List<ProjectDetailInfo> content = result.getContent();

		for (int i = 0; i < content.size(); i++) {
			ProjectDetailInfo r = content.get(i);
			assertThat(r.projectType()).isEqualTo(ProjectType.TEAM.name());
			assertThat(r.progress()).isLessThan(100);
			assertThat(r.projectToken()).isEqualTo(recentlyCreatedSortProject.get(i));
		}

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		User user = getUser(13L);

		ProjectSideMenuResponse response = sut.getProjectSideMenuDetails(projectToken, user);

		assertThat(response.progress()).isEqualTo("60.0");
		assertThat(response.teamMembers().size()).isEqualTo(3);

	}


	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		User user = getUser(13L);

		String response = sut.registerKeyResult(new ProjectKeyResultSaveDto(projectToken, keyResultName), user);

		assertThat(response).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_기간_종료후_핵심결과_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_K4e8a5s7d6lb6421";
		String keyResultName = "keyResult";

		User user = getUser(12L);

		assertThatThrownBy(() -> sut.registerKeyResult(new ProjectKeyResultSaveDto(projectToken, keyResultName), user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 팀원이_프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		User user = getUser(3L);

		assertThatThrownBy(() -> sut.registerKeyResult(new ProjectKeyResultSaveDto(projectToken, keyResultName), user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트에_핵심결과_3개_이상_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_Kiwqnp1Nq6lbTNn0";
		String keyResultName = "keyResult";

		User user = getUser(2L);

		assertThatThrownBy(() -> sut.registerKeyResult(new ProjectKeyResultSaveDto(projectToken, keyResultName), user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.KEYRESULT_LIMIT_EXCEED.getMessage());

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		ProjectInitiativeSaveCommand requestDto = new ProjectInitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		User user = getUser(3L);

		String response = sut.registerInitiative(requestDto, user);


		Initiative initiativeToken = em.createQuery(
				"select i from Initiative i where i.initiativeToken = :initiativeToken", Initiative.class)
			.setParameter("initiativeToken", response)
			.getSingleResult();
		assertThat(initiativeToken.getInitiativeToken()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_추가시_프로젝트_진척도_변경된다() throws Exception {

		ProjectInitiativeSaveCommand requestDto = new ProjectInitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		User user = getUser(3L);

		String response = sut.registerInitiative(requestDto, user);


		Project project = em.createQuery(
				"select p from Project p where p.id = :id", Project.class)
			.setParameter("id", 99998L)
			.getSingleResult();
		assertThat(project.getProgress()).isEqualTo(50.0);
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		String response = sut.initiativeFinished(initiativeToken,  getUser(11L));

		assertThat(response).isEqualTo("ini_ixYjj5nODqtb3AH8");
	}


	private User getUser(long userSeq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", userSeq)
			.getSingleResult();
		return user;
	}

}