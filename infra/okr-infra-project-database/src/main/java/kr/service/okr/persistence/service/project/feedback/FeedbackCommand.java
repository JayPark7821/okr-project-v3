package kr.service.okr.persistence.service.project.feedback;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.project.feedback.FeedbackJpaEntity;
import kr.service.okr.persistence.repository.project.feedback.FeedbackJpaRepository;
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
