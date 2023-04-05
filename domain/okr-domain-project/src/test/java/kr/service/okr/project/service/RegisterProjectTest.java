package kr.service.okr.project.service;

import static kr.service.okr.project.repository.TestProjectRepository.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okr.OkrMessages;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.project.aggregate.team.domain.TeamMember;
import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.repository.TestProjectRepository;
import kr.service.okr.project.usecase.RegisterProjectUseCase;

class RegisterProjectTest {

	private RegisterProjectUseCase sut;
	private ProjectCommand projectCommand;

	@BeforeEach
	void setUp() {
		TestProjectRepository.clear();
		this.projectCommand = new TestProjectRepository.TestProjectCommand();
		sut = new RegisterProject(projectCommand);
	}

	@Test
	@DisplayName("팀원없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		LocalDate projectSdt = LocalDate.now();
		LocalDate projectEdt = LocalDate.now().plusDays(10);

		final String result = sut.registerProject(
			new RegisterProjectUseCase.Command("object", projectSdt, projectEdt, 1L, List.of()));

		assertThat(result).containsPattern(Pattern.compile("project-[a-zA-Z0-9]{12}"));
		assertThat(persistence.get(1L).getTeamMember().size()).isEqualTo(1L);
		assertThat(persistence.get(1L).getTeamMember().get(0).getProjectRoleType()).isEqualTo(ProjectRoleType.LEADER);

	}

	@Test
	@DisplayName("팀원을 추가해 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		LocalDate projectSdt = LocalDate.now();
		LocalDate projectEdt = LocalDate.now().plusDays(10);

		final String result = sut.registerProject(
			new RegisterProjectUseCase.Command("object", projectSdt, projectEdt, 1L, List.of(2L, 3L)));

		assertThat(result).containsPattern(Pattern.compile("project-[a-zA-Z0-9]{12}"));
		assertThat(persistence.get(1L).getTeamMember().size()).isEqualTo(3L);
		assertThat(persistence.get(1L).getTeamMember().stream().map(TeamMember::getProjectRoleType).toList())
			.containsExactlyInAnyOrder(ProjectRoleType.LEADER, ProjectRoleType.MEMBER, ProjectRoleType.MEMBER);
	}

	@Test
	@DisplayName("프로젝트 등록 요청자가 팀원에 포함되 프로젝트를 생성하면 기대하는 응답(Exception)을 반환한다.")
	void create_project_with_team_members_which_contains_leader() throws Exception {

		LocalDate projectSdt = LocalDate.now();
		LocalDate projectEdt = LocalDate.now().plusDays(10);

		assertThatThrownBy(() -> sut.registerProject(
			new RegisterProjectUseCase.Command("object", projectSdt, projectEdt, 1L, List.of(1L, 3L)))
		)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(OkrMessages.LEADER_IS_IN_TEAM_MEMBER.getMsg());

	}
}