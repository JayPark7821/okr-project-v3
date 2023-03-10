package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;

public record FeedbackDetailInfo(
	String feedbackToken,
	String opinion,
	FeedbackType grade,
	String writerName,
	String writerJob,
	String profileImage
) {

	public FeedbackDetailInfo(Feedback feedback) {
		this(
			feedback.getFeedbackToken(),
			feedback.getOpinion(),
			feedback.getGrade(),
			feedback.getTeamMember().getUser().getUsername(),
			feedback.getTeamMember().getUser().getJobField().getTitle(),
			feedback.getTeamMember().getUser().getProfileImage()
		);
	}
}
