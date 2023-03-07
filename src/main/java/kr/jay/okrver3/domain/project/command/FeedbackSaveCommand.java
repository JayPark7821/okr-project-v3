package kr.jay.okrver3.domain.project.command;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;

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
