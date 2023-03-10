package kr.jay.okrver3.interfaces.project.response;

import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;

public record FeedbackResponse(
	String feedbackToken,
	String opinion,
	FeedbackType grade,
	String writerName,
	String writerJob,
	String profileImage
) {
}
