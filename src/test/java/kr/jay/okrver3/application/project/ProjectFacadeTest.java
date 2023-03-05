package kr.jay.okrver3.application.project;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.notification.service.impl.NotificationServiceImpl;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectInitiativeInfo;
import kr.jay.okrver3.domain.project.service.impl.ProjectServiceImpl;
import kr.jay.okrver3.domain.project.validator.InitiativeDoneValidator;
import kr.jay.okrver3.domain.project.validator.ProjectInitiativeDateValidator;
import kr.jay.okrver3.domain.project.validator.ProjectKeyResultCountValidator;
import kr.jay.okrver3.domain.project.validator.ProjectLeaderValidator;
import kr.jay.okrver3.domain.project.validator.ProjectPeriodValidator;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessor;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.impl.UserServiceImpl;
import kr.jay.okrver3.infrastructure.notification.NotificationJDBCRepository;
import kr.jay.okrver3.infrastructure.project.ProjectQueryDslRepository;
import kr.jay.okrver3.infrastructure.project.ProjectRepositoryImpl;
import kr.jay.okrver3.interfaces.project.ProjectInitiativeResponse;
import kr.jay.okrver3.interfaces.project.ProjectKeyResultSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectSideMenuResponse;
import kr.jay.okrver3.interfaces.project.TeamMemberInviteRequestDto;

@DataJpaTest
@Import({ProjectFacade.class, ProjectServiceImpl.class, UserServiceImpl.class,
	NotificationServiceImpl.class, NotificationJDBCRepository.class, ProjectRepositoryImpl.class,
	ProjectQueryDslRepository.class,
	ProjectValidateProcessor.class, ProjectLeaderValidator.class,
	ProjectKeyResultCountValidator.class, ProjectPeriodValidator.class, ProjectInitiativeDateValidator.class,
	InitiativeDoneValidator.class})
class ProjectFacadeTest {

	@Autowired
	private ProjectFacade sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원 추가 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = getUser(1L);

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String projectToken = sut.registerProject(
			new ProjectMasterSaveDto("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), null), user);

		Project result =
			em.createQuery("select n from Project n where n.objective =: objective", Project.class)
				.setParameter("objective", "projectObjective")
				.getSingleResult();

		assertThat(result.getTeamMember().size()).isEqualTo(1);
		assertThat(projectToken).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성시 팀원을 같이 입력하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		User user = getUser(1L);

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String projectToken = sut.registerProject(
			new ProjectMasterSaveDto("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), user);

		Project result =
			em.createQuery("select n from Project n where n.objective =: objective", Project.class)
				.setParameter("objective", "projectObjective")
				.getSingleResult();

		assertThat(result.getTeamMember().size()).isEqualTo(2);

		assertThat(projectToken).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
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
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환하고 알림을 저장한다.")
	void invite_team_member() throws Exception {

		User user = getUser(1L);

		String response = sut.inviteTeamMember(
			new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "fakeAppleEmail"), user);

		assertThat(response).isEqualTo("fakeAppleEmail");

		List<Notification> result = em.createQuery("select n from Notification n", Notification.class)
			.getResultList();
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getMsg()).isEqualTo(
			Notifications.NEW_TEAM_MATE.getMsg("fakeAppleName", "projectObjective"));

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		User user = getUser(1L);

		final String response = sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, user);

		assertThat(response).isEqualTo(memberEmail);
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		User user = getUser(2L);

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		User user = getUser(3L);

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {

		String wrongEmailAdd = "wrongEmailAdd";
		User user = getUser(1L);

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", wrongEmailAdd, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";
		User user = getUser(1L);

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", teamMemberEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "apple@apple.com";
		User user = getUser(1L);

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", userEmail, user))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
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
	void 팀원이_프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		String keyResultName = "keyResult";

		User user = getUser(13L);

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

		assertThat(response).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}


	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		String response = sut.initiativeFinished(initiativeToken,  getUser(11L));

		assertThat(response).isEqualTo("ini_ixYjj5nODfeab3AH8");
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 핵심결과토큰으로_행동전략_리스트_조회시_기대하는_응답을_리턴한다() throws Exception {
		String keyResultToken = "ini_ixYjj5nODfeab3AH8";


		Page<ProjectInitiativeInfo> response =
			sut.getInitiativeByKeyResultToken(keyResultToken, getUser(11L),PageRequest.of(0, 5));

		assertThat(response.getTotalElements()).isEqualTo(2);
		List<ProjectInitiativeInfo> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {

		}

	}

	private User getUser(Long seq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", seq)
			.getSingleResult();
		return user;
	}

}