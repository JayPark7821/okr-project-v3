package kr.service.okr.interfaces.project.response;

import kr.service.okr.domain.project.aggregate.feedback.FeedbackType;

public record FeedbackDetailResponse(
	String initiativeToken,
	String feedbackToken,
	String opinion,
	FeedbackType grade,
	String writerName,
	String writerJob,
	String profileImage
) {
}
