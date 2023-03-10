package kr.jay.okrver3.interfaces.project.response;

import java.util.List;

public record IniFeedbackResponse(
	boolean myInitiative,
	boolean wroteFeedback,
	List<FeedbackResponse> feedback
) {
}
