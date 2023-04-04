package kr.service.persistence.service.project.aggregate.feedback;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.project.aggregate.feedback.FeedbackJpaEntity;
import kr.service.persistence.repository.project.aggregate.feedback.FeedbackJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackCommand {

	private final FeedbackJpaRepository feedbackJpaRepository;

	public FeedbackJpaEntity save(FeedbackJpaEntity feedback) {
		return feedbackJpaRepository.save(feedback);
	}

}
