package kr.jay.okrver3.application.feedback;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.feedback.FeedbackSaveCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackFacade {
	public String registerFeedback(FeedbackSaveCommand of, User userFromAuthentication) {
		throw new UnsupportedOperationException(
			"kr.jay.okrver3.application.feedback.FeedbackFacade.registerFeedback())");
	}
}
