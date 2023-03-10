package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;

public record FeedbackDetailInfo(
	String feedbackToken,
	String opinion,
	FeedbackType grade,
	String writerName,
	String writerJob,
	String profileImage
) {
}
