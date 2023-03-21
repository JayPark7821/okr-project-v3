package kr.service.okr.domain.project.aggregate.feedback;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackRepository {
	Feedback save(Feedback feedback);

	List<Feedback> findInitiativeFeedbacksByInitiativeToken(String initiativeToken);

	Page<Feedback> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable);
}
