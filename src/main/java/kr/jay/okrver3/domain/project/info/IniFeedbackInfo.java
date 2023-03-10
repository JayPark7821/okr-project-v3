package kr.jay.okrver3.domain.project.info;

import java.util.List;

public record IniFeedbackInfo(
	boolean myInitiative,
	boolean wroteFeedback,
	List<FeedbackDetailInfo> feedback
) {
}
