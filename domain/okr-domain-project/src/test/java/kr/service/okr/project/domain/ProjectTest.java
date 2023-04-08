package kr.service.okr.project.domain;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import kr.service.okr.team.domain.TeamMember;

class ProjectTest {
	private static final Long LEADER = 1L;
	private static final Long FIRST_MEMBER = 2L;
	private static final Long SECOND_MEMBER = 3L;

	@Value("${project.objective.max-length}")
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
		final Project project = getProject();
		final Project endedProject = getEndedProject();
		final Project finishedProject = getFinishedProject();

		// when
		assertThatThrownBy(() -> endedProject.createAndAddMemberOf(FIRST_MEMBER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

		assertThatThrownBy(() -> finishedProject.createAndAddMemberOf(FIRST_MEMBER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.PROJECT_IS_FINISHED.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(LEADER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(SECOND_MEMBER, FIRST_MEMBER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(FIRST_MEMBER, SECOND_MEMBER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		project.createAndAddMemberOf(FIRST_MEMBER, LEADER);

		assertThatThrownBy(() -> project.createAndAddMemberOf(FIRST_MEMBER, LEADER))
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

	@Test
	void registerKeyResult() throws Exception {
		//given
		final Project project = getProject();
		final Project endedProject = getEndedProject();
		final Project finishedProject = getFinishedProject();
		final String keyResultName = "new keyResultName";
		//when
		assertThatThrownBy(() -> endedProject.addKeyResult(keyResultName, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

		assertThatThrownBy(() -> finishedProject.addKeyResult(keyResultName, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.PROJECT_IS_FINISHED.getMessage());

		assertThatThrownBy(() -> project.addKeyResult(keyResultName, FIRST_MEMBER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		IntStream.range(0, 3)
			.forEach(i -> project.addKeyResult(keyResultName + i, LEADER));

		assertThatThrownBy(() -> project.addKeyResult(keyResultName, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.MAX_KEYRESULT_COUNT_EXCEEDED.getMessage());

		//then
		assertThat(project.getKeyResults()).hasSize(3);
	}

	private static Project getEndedProject() throws Exception {
		final Project project = new Project("objective", generateDate(-10), generateDate(1));
		project.createAndAddLeader(LEADER);
		final Field endDate = project.getClass().getDeclaredField("endDate");
		endDate.setAccessible(true);
		endDate.set(project, generateDate(-1));
		endDate.setAccessible(false);
		return project;
	}

	private static Project getProject() {
		final Project project = new Project("objective", generateDate(0), generateDate(10));
		project.createAndAddLeader(LEADER);
		return project;
	}

	private static Project getFinishedProject() {
		final Project project = new Project("objective", generateDate(-10), generateDate(1));
		project.createAndAddLeader(LEADER);
		project.makeProjectFinished();
		return project;
	}

	private static LocalDate generateDate(int days) {
		return days >= 0 ? LocalDate.now().plusDays(days) : LocalDate.now().minusDays(Math.abs(days));
	}
}