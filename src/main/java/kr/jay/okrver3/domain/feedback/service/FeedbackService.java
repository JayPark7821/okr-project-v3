package kr.jay.okrver3.domain.feedback.service;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.feedback.FeedbackSaveCommand;

public interface FeedbackService {
	String registerFeedback(FeedbackSaveCommand command, User requester);
}
