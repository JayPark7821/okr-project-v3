package kr.jay.okrver3.domain.feedback.service;

import kr.jay.okrver3.domain.feedback.Feedback;

public interface FeedbackRepository {
	Feedback save(Feedback feedback);
}
