package kr.jay.okrver3.domain.project.aggregate.feedback;

import java.util.List;

import com.google.common.io.Files;

public interface FeedbackRepository {
	Feedback save(Feedback feedback);

	List<Feedback> findInitiativeFeedbacksByInitiativeToken(String initiativeToken);
}
