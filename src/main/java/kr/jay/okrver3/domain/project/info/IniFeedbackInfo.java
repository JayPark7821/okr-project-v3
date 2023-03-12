package kr.jay.okrver3.domain.project.info;

import java.util.List;
import java.util.Map;

import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;

public record IniFeedbackInfo(
	boolean myInitiative,
	boolean wroteFeedback,
	Map<FeedbackType, Long> gradeCount,

	List<FeedbackDetailInfo> feedback
) {
}
