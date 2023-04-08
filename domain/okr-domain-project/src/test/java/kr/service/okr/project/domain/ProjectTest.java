package kr.service.okr.project.domain;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import kr.service.okr.team.domain.TeamMember;

class ProjectTest {
	private RegisterProjectUseCase sut;

	@Test
	void project_constructor_test() throws Exception {
		final String tooLongObjective = IntStream.range(0, 51)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());

		assertThatThrownBy(() -> new Project(null, generateDate(0), generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.OBJECTIVE_IS_REQUIRED.getMessage());

		assertThatThrownBy(() -> new Project(tooLongObjective, generateDate(0), generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.OBJECTIVE_IS_TOO_LONG.getMessage());

		assertThatThrownBy(() -> new Project("objective", null, generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_START_DATE_IS_REQUIRED.getMessage());

		assertThatThrownBy(() -> new Project("objective", generateDate(0), null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_END_DATE_IS_REQUIRED.getMessage());

		assertThatThrownBy(() -> new Project("objective", generateDate(10), generateDate(0)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage());

		assertThatThrownBy(() -> new Project("objective", generateDate(-10), generateDate(-1)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_END_DATE_IS_BEFORE_TODAY.getMessage());

		assertThat(new Project("objective", generateDate(0), generateDate(10)).getProjectToken())
			.containsPattern(Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	void inviteNewTeamMember() throws Exception {
		// given
		final Long projectLeaderSeq = 1L;
		final Long projectMemberSeq = 2L;
		final Long projectMemberSeq2 = 3L;
		final Project project = new Project("objective", generateDate(0), generateDate(10));
		final Project endedProject = new Project("objective", generateDate(-10), generateDate(1));
		final Project finishedProject = new Project("objective", generateDate(-10), generateDate(1));
		final Field endDate = endedProject.getClass().getDeclaredField("endDate");
		endDate.setAccessible(true);
		endDate.set(endedProject, generateDate(-1));
		endDate.setAccessible(false);
		finishedProject.makeProjectFinished();
		project.createAndAddLeader(projectLeaderSeq);
		endedProject.createAndAddLeader(projectLeaderSeq);

		// when
		assertThatThrownBy(() -> endedProject.createAndAddMemberOf(projectMemberSeq, projectLeaderSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

		assertThatThrownBy(() -> finishedProject.createAndAddMemberOf(projectMemberSeq, projectLeaderSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.PROJECT_IS_FINISHED.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(projectLeaderSeq, projectLeaderSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(projectMemberSeq2, projectMemberSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(projectMemberSeq, projectMemberSeq2))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		project.createAndAddMemberOf(projectMemberSeq, projectLeaderSeq);

		assertThatThrownBy(() -> project.createAndAddMemberOf(projectMemberSeq, projectLeaderSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());

		//then
		final List<TeamMember> teamMembers = project.getTeamMember();
		assertThat(teamMembers).hasSize(2);
		assertThat(teamMembers.stream()
			.filter(teamMember ->
				teamMember.getProjectRoleType().equals(ProjectRoleType.MEMBER) &&
					teamMember.getUserSeq().equals(2L)).findAny())
			.isPresent();

	}

	private static LocalDate generateDate(int days) {
		return days >= 0 ? LocalDate.now().plusDays(days) : LocalDate.now().minusDays(Math.abs(days));
	}
}