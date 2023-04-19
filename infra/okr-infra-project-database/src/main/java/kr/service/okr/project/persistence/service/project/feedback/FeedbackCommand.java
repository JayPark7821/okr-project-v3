package kr.service.okr.project.persistence.service.project.feedback;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.persistence.entity.project.feedback.FeedbackJpaEntity;
import kr.service.okr.project.persistence.repository.project.feedback.FeedbackJpaRepository;
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
