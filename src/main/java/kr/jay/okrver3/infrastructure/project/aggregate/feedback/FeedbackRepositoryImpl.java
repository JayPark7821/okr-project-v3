package kr.jay.okrver3.infrastructure.project.aggregate.feedback;

import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {

	private final FeedbackJpaRepository feedbackJpaRepository;

	@Override
	public Feedback save(Feedback feedback) {
		return feedbackJpaRepository.save(feedback);
	}
}
