package kr.jay.okrver3.infrastructure.project.aggregate.feedback;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackRepository;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {

	private final FeedbackJpaRepository feedbackJpaRepository;
	private final FeedbackQueryDslRepository feedbackQueryDslRepository;

	@Override
	public Feedback save(Feedback feedback) {
		return feedbackJpaRepository.save(feedback);
	}

	@Override
	public List<Feedback> findInitiativeFeedbacksByInitiativeToken(String initiativeToken) {
		return feedbackJpaRepository.findInitiativeFeedbacksByInitiativeToken(initiativeToken);
	}

	@Override
	public Page<Feedback> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable) {
		return feedbackQueryDslRepository.getRecievedFeedback(range, userSeq, pageable);
	}
}
