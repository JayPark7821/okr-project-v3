package kr.service.okr.project.domain;

import static kr.service.okr.exception.ErrorCode.*;
import static kr.service.okr.project.domain.ProjectValidator.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.ProjectType;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

/*
 TODO :
  1. 프로젝트 삭제
  3. 행동전략 삭제
  4. 행동전략 수정
  5. 핵심결과 삭제
  6. 핵심결과 수정
  7. 피드백 등록
  8. 피드백 삭제
  9. 피드백 수정

 */
@Getter
public class Project {

	private static final String PROJECT_TOKEN_PREFIX = "project-";

	private Long id;
	private String projectToken;
	private List<TeamMember> teamMember = new ArrayList<>();
	private List<KeyResult> keyResults = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
	private ProjectType type = ProjectType.SINGLE;
	private String objective;
	private double progress = 0.0D;
	private boolean finished = false;

	public Project(final String objective, final LocalDate startDate, final LocalDate endDate) {
		// validateAddingNewProject(objective, startDate, endDate);
		this.projectToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_TOKEN_PREFIX);
		this.startDate = startDate;
		this.endDate = endDate;
		this.objective = objective;
	}

	@Builder
	private Project(
		final Long id,
		final String projectToken,
		final List<TeamMember> teamMember,
		final List<KeyResult> keyResults,
		final LocalDate startDate,
		final LocalDate endDate,
		final ProjectType type,
		final String objective,
		final double progress,
		final boolean finished
	) {

		this.id = id;
		this.projectToken = projectToken;
		this.teamMember = teamMember == null ? new ArrayList<>() : teamMember;
		this.keyResults = keyResults;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.objective = objective;
		this.progress = progress;
		this.finished = finished;
	}

	public void createAndAddLeader(final Long leaderSeq) {
		validateAddingProjectLeader();
		this.teamMember.add(
			TeamMember.createLeader(leaderSeq, this)
		);
	}

	public void createAndAddMemberOf(final Long memberSeq, final Long leaderSeq) {
		validateAddingNewTeamMember(memberSeq, leaderSeq);
		this.teamMember.add(
			TeamMember.createMember(memberSeq, this)
		);
	}

	// TODO protected로 변경
	public void addTeamMember(final TeamMember member) {
		this.teamMember.add(member);
	}

	public String addKeyResult(final String keyResultName, final Long leader) {
		validateAddingNewKeyResult(keyResultName, leader);

		final KeyResult keyResult =
			new KeyResult(keyResultName, this.id, this.keyResults.size() + 1, new ArrayList<>());
		this.keyResults.add(keyResult);

		return keyResult.getKeyResultToken();
	}

	public void makeProjectFinished() {
		this.finished = true;
	}

	public String addInitiative(
		final String keyResultToken,
		final String initiativeName,
		final Long memberSeq,
		final String initiativeDetail,
		final LocalDate startDate,
		final LocalDate endDate
	) {
		validateAddingNewInitiative(initiativeName, initiativeDetail, startDate, endDate);

		return getKeyResult(keyResultToken)
			.addInitiative(
				initiativeName,
				getMemberBy(memberSeq),
				initiativeDetail,
				startDate,
				endDate
			);
	}

	public void updateProject(String objective, LocalDate startDate, LocalDate endDate, Long userSeq) {
		validateUpdateProject(objective, startDate, endDate, userSeq);
		this.objective = objective != null ? objective : this.objective;
		this.startDate = startDate != null ? startDate : this.startDate;
		this.endDate = endDate != null ? endDate : this.endDate;

	}

	private TeamMember getMemberBy(final Long memberSeq) {
		return this.teamMember.stream()
			.filter(teamMember -> teamMember.getUserSeq().equals(memberSeq))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(INVALID_PROJECT_TOKEN));
	}

	private KeyResult getKeyResult(final String keyResultToken) {
		return this.keyResults.stream()
			.filter(kr -> kr.getKeyResultToken().equals(keyResultToken))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(INVALID_KEYRESULT_TOKEN));
	}

	//====================================  validate  =================================================
	private void validateAddingNewInitiative(
		final String initiativeName,
		final String initiativeDetail,
		final LocalDate startDate,
		final LocalDate endDate
	) {

		validateProjectInProgress(this);
		validateFinishedProject(this);

		validateInitiativeNameInput(initiativeName);
		validateInitiativeDetailInput(initiativeDetail);
		validateInitiativeDuration(this, startDate, endDate);
	}

	private void validateUpdateProject(final String objective, final LocalDate startDate, final LocalDate endDate,
		Long userSeq) {
		validateProjectLeader(this, userSeq);
		validateFinishedProject(this);
		validateAndUpdateDates(this, startDate, endDate);
		validateAndUpdateObjective(objective);
	}

	private void validateAddingNewProject(final String objective, final LocalDate startDate, final LocalDate endDate) {
		validateObjectInput(objective);
		validateProjectDuration(startDate, endDate);
	}

	private void validateAddingNewTeamMember(final Long memberSeq, final Long leaderSeq) {
		validateFinishedProject(this);
		validateProjectInProgress(this);
		validateProjectLeader(this, leaderSeq);
		validateSelfInviting(memberSeq, leaderSeq);
		validateAlreadyTeamMember(this, memberSeq);
	}

	private void validateAddingNewKeyResult(final String keyResultName, final Long leader) {
		validateProjectLeader(this, leader);
		validateProjectInProgress(this);
		validateFinishedProject(this);
		validateMaxKeyResultCountExceeded(this);
		validateMaxKeyResultLength(keyResultName);
	}

	private void validateAddingProjectLeader() {
		validateFinishedProject(this);
		validateProjectHasLeader(this);
	}

}
