package kr.service.okr.domain.project.command;

import kr.service.okr.domain.project.aggregate.feedback.Feedback;
import kr.service.okr.domain.project.aggregate.feedback.FeedbackType;
import kr.service.okr.domain.project.aggregate.initiative.Initiative;
import kr.service.okr.domain.project.aggregate.team.TeamMember;

public record FeedbackSaveCommand(String opinion, String grade, String initiativeToken) {
	public Feedback toEntity(Initiative initiative, Long requesterSeq) {
		TeamMember requestedTeamMember = initiative.getProject()
			.getTeamMember()
			.stream()
			.filter(teamMember -> teamMember.getUserSeq().equals(requesterSeq))
			.findFirst()
			.orElseThrow();

		return Feedback.builder()
			.opinion(opinion)
			.grade(FeedbackType.of(grade))
			.initiative(initiative)
			.teamMember(requestedTeamMember)
			.build();
	}
}
