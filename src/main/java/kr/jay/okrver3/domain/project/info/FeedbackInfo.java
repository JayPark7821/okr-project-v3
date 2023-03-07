package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;

public record FeedbackInfo(String feedbackToken, String initiativeName, Long initiativeUserSeq,
						   String feedbackUserName) {

	public FeedbackInfo(Feedback feedback) {
		this(feedback.getFeedbackToken(),
			feedback.getInitiative().getName(),
			feedback.getInitiative().getTeamMember().getUserSeq(),
			feedback.getTeamMember().getUser().getUsername()
		);
	}
}
