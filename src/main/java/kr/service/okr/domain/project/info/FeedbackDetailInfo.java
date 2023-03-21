package kr.service.okr.domain.project.info;

import kr.service.okr.domain.project.aggregate.feedback.Feedback;
import kr.service.okr.domain.project.aggregate.feedback.FeedbackType;

public record FeedbackDetailInfo(
	String initiativeToken,
	String feedbackToken,
	String opinion,
	FeedbackType grade,
	String writerName,
	String writerJob,
	String profileImage
) {

	public FeedbackDetailInfo(Feedback feedback) {
		this(
			feedback.getInitiative().getInitiativeToken(),
			feedback.getFeedbackToken(),
			feedback.getOpinion(),
			feedback.getGrade(),
			feedback.getTeamMember().getUser().getUsername(),
			feedback.getTeamMember().getUser().getJobField().getTitle(),
			feedback.getTeamMember().getUser().getProfileImage()
		);
	}
}
