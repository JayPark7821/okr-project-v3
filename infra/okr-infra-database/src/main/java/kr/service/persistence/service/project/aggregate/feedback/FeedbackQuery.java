package kr.service.persistence.service.project.aggregate.feedback;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.model.project.SearchRange;
import kr.service.persistence.entity.project.aggregate.feedback.FeedbackJpaEntity;
import kr.service.persistence.repository.project.aggregate.feedback.FeedbackJpaRepository;
import kr.service.persistence.repository.project.aggregate.feedback.FeedbackQueryDslRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackQuery {

	private final FeedbackJpaRepository feedbackJpaRepository;
	private final FeedbackQueryDslRepository feedbackQueryDslRepository;

	public List<FeedbackJpaEntity> findInitiativeFeedbacksByInitiativeToken(String initiativeToken) {
		return feedbackJpaRepository.findInitiativeFeedbacksByInitiativeToken(initiativeToken);
	}

	public Page<FeedbackJpaEntity> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable) {
		return feedbackQueryDslRepository.getRecievedFeedback(range, userSeq, pageable);
	}
}
