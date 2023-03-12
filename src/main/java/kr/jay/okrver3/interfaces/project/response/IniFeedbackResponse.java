package kr.jay.okrver3.interfaces.project.response;

import java.util.List;
import java.util.Map;

import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;

public record IniFeedbackResponse(
	boolean myInitiative,
	boolean wroteFeedback,
	Map<FeedbackType, Long> gradeCount,
	List<FeedbackDetailResponse> feedback
) {
}
