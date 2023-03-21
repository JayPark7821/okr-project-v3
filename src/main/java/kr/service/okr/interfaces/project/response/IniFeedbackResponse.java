package kr.service.okr.interfaces.project.response;

import java.util.List;
import java.util.Map;

import kr.service.okr.domain.project.aggregate.feedback.FeedbackType;

public record IniFeedbackResponse(
	boolean myInitiative,
	boolean wroteFeedback,
	Map<FeedbackType, Long> gradeCount,
	List<FeedbackDetailResponse> feedback
) {
}
