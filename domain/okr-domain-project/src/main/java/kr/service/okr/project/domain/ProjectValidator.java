package kr.service.okr.project.domain;

import static kr.service.okr.exception.ErrorCode.*;

import java.time.LocalDate;
import java.util.Collection;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.team.ProjectRoleType;

public class ProjectValidator {

	protected static final int MAX_KEYRESULT_COUNT = 3;
	protected static final int MAX_OBJECTIVE_LENGTH = 50;
	protected static final int MAX_KERSULT_NAME_LENGTH = 50;
	protected static final int MAX_INITIATIVE_NAME_LENGTH = 50;
	protected static final int MAX_INITIATIVE_DETAIL_LENGTH = 200;

	protected static void validateProjectInProgress(final Project project) {
		if (project.getEndDate().isBefore(LocalDate.now()))
			throw new OkrApplicationException(ErrorCode.NOT_UNDER_PROJECT_DURATION);
	}

	protected static void validateFinishedProject(final Project project) {
		if (project.isFinished())
			throw new OkrApplicationException(ErrorCode.PROJECT_IS_FINISHED);
	}

	protected static void validateProjectLeader(final Project project, final Long leaderSeq) {
		project.getTeamMember().stream()
			.filter(member ->
				member.getUserSeq().equals(leaderSeq) &&
					member.getProjectRoleType().equals(ProjectRoleType.LEADER))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	protected static void validateProjectDuration(final LocalDate startDate, final LocalDate endDate) {
		if (startDate == null)
			throw new OkrApplicationException(PROJECT_START_DATE_IS_REQUIRED);
		if (endDate == null)
			throw new OkrApplicationException(PROJECT_END_DATE_IS_REQUIRED);
		if (startDate.isAfter(endDate))
			throw new OkrApplicationException(PROJECT_START_DATE_IS_AFTER_END_DATE);
		if (endDate.isBefore(LocalDate.now()))
			throw new OkrApplicationException(PROJECT_END_DATE_IS_BEFORE_TODAY);
	}

	protected static void validateObjectInput(final String objective) {
		if (objective == null)
			throw new OkrApplicationException(OBJECTIVE_IS_REQUIRED);
		if (objective.length() > MAX_OBJECTIVE_LENGTH || objective.length() == 0)
			throw new OkrApplicationException(OBJECTIVE_WRONG_INPUT_LENGTH);
	}

	protected static void validateAlreadyTeamMember(final Project project, final Long memberSeq) {
		if (project.getTeamMember().stream().anyMatch(member -> member.getUserSeq().equals(memberSeq)))
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);
	}

	protected static void validateSelfInviting(final Long memberSeq, final Long leaderSeq) {
		if (memberSeq.equals(leaderSeq))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);
	}

	protected static void validateMaxKeyResultLength(final String keyResultName) {
		if (keyResultName.length() >= MAX_KERSULT_NAME_LENGTH)
			throw new OkrApplicationException(KEYRESULT_NAME_WRONG_INPUT_LENGTH);
	}

	protected static void validateMaxKeyResultCountExceeded(final Project project) {
		if (project.getKeyResults().size() >= MAX_KEYRESULT_COUNT)
			throw new OkrApplicationException(MAX_KEYRESULT_COUNT_EXCEEDED);
	}

	protected static void validateProjectHasLeader(final Project project) {
		if (project.getTeamMember().stream()
			.anyMatch(teamMember -> teamMember.getProjectRoleType().equals(ProjectRoleType.LEADER)))
			throw new OkrApplicationException(ErrorCode.PROJECT_ALREADY_HAS_LEADER);
	}

	protected static void validateInitiativeDuration(final Project project, final LocalDate startDate,
		final LocalDate endDate) {
		if (startDate == null)
			throw new OkrApplicationException(INITIATIVE_START_DATE_IS_REQUIRED);
		if (endDate == null)
			throw new OkrApplicationException(INITIATIVE_END_DATE_IS_REQUIRED);
		if (endDate.isBefore(project.getStartDate()) ||
			endDate.isAfter(project.getEndDate()) ||
			startDate.isBefore(project.getStartDate()) ||
			startDate.isAfter(project.getEndDate())
		) {
			throw new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_DATE);
		}
	}

	protected static void validateInitiativeDetailInput(final String initiativeDetail) {
		if (initiativeDetail == null)
			throw new OkrApplicationException(INITIATIVE_DETAIL_IS_REQUIRED);
		if (initiativeDetail.length() > MAX_INITIATIVE_DETAIL_LENGTH || initiativeDetail.length() == 0)
			throw new OkrApplicationException(INITIATIVE_DETAIL_WRONG_INPUT_LENGTH);

	}

	protected static void validateInitiativeNameInput(final String initiativeName) {
		if (initiativeName == null)
			throw new OkrApplicationException(INITIATIVE_NAME_IS_REQUIRED);
		if (initiativeName.length() > MAX_INITIATIVE_NAME_LENGTH || initiativeName.length() == 0)
			throw new OkrApplicationException(INITIATIVE_NAME_WRONG_INPUT_LENGTH);

	}

	protected static void validateAndUpdateDates(Project project, LocalDate startDate, LocalDate endDate) {
		validateProjectDates(project, startDate, endDate);
		validateInitiativeDates(
			project,
			startDate != null ? startDate : project.getStartDate(),
			endDate != null ? endDate : project.getEndDate()
		);
	}

	private static void validateProjectDates(Project project, LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw new OkrApplicationException(PROJECT_START_DATE_IS_AFTER_END_DATE);
		} else if (startDate != null && startDate.isAfter(project.getEndDate())) {
			throw new OkrApplicationException(PROJECT_START_DATE_IS_AFTER_END_DATE);
		} else if (endDate != null && project.getStartDate().isAfter(endDate)) {
			throw new OkrApplicationException(PROJECT_START_DATE_IS_AFTER_END_DATE);
		}
	}

	private static void validateInitiativeDates(Project project, LocalDate startDate, LocalDate endDate) {
		project.getKeyResults().stream()
			.map(KeyResult::getInitiative)
			.flatMap(Collection::stream)
			.filter(initiative -> {
				return initiative.getStartDate().isBefore(startDate) || initiative.getEndDate().isAfter(endDate);
			})
			.findFirst()
			.ifPresent(initiative -> {
				throw new OkrApplicationException(ErrorCode.INITIATIVE_DATES_WILL_BE_OVER_PROJECT_DATES);
			});
	}

	protected static void validateAndUpdateObjective(String objective) {
		if (objective != null && (objective.length() > MAX_OBJECTIVE_LENGTH || objective.isEmpty())) {
			throw new OkrApplicationException(OBJECTIVE_WRONG_INPUT_LENGTH);
		}
	}

}
