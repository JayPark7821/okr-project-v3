package kr.jay.okrver3.interfaces.feedback;

import kr.jay.okrver3.domain.feedback.Feedback;
import kr.jay.okrver3.domain.feedback.FeedbackType;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.team.TeamMember;
import kr.jay.okrver3.domain.user.User;

public record FeedbackSaveCommand(String opinion, String grade, String projectToken, String initiativeToken) {
	public Feedback toEntity(Initiative initiative, User requester) {
		TeamMember requestedTeamMember = initiative.getProject()
			.getTeamMember()
			.stream()
			.filter(teamMember -> teamMember.getUser().equals(requester))
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
