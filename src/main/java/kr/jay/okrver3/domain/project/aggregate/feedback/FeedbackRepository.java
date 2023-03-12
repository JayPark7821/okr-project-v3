package kr.jay.okrver3.domain.project.aggregate.feedback;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.common.io.Files;

public interface FeedbackRepository {
	Feedback save(Feedback feedback);

	List<Feedback> findInitiativeFeedbacksByInitiativeToken(String initiativeToken);

	Page<Feedback> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable);
}
