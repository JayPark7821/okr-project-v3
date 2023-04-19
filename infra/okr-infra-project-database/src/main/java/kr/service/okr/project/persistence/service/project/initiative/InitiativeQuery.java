package kr.service.okr.project.persistence.service.project.initiative;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.persistence.entity.project.initiative.InitiativeJpaEntity;
import kr.service.okr.project.persistence.repository.project.initiative.InitiativeJpaRepository;
import kr.service.okr.project.persistence.repository.project.initiative.InitiativeQueryDslRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InitiativeQuery {

	private final InitiativeJpaRepository initiativeJpaRepository;
	private final InitiativeQueryDslRepository initiativeQueryDslRepository;

	public Optional<InitiativeJpaEntity> findInitiativeByInitiativeTokenAndUserSeq(String initiativeToken,
		Long userSeq) {
		return initiativeJpaRepository.findInitiativeByInitiativeTokenAndUserSeq(initiativeToken, userSeq);
	}

	public Optional<InitiativeJpaEntity> findInitiativeDetailByInitiativeTokenAndUserSeq(String initiativeToken,
		Long userSeq) {
		return initiativeJpaRepository.findInitiativeDetailByInitiativeTokenAndUserSeq(initiativeToken, userSeq);
	}

	public List<InitiativeJpaEntity> findInitiativeByDate(LocalDate searchDate, Long userSeq) {
		return initiativeJpaRepository.findInitiativeByDateAndUserSeq(searchDate, userSeq);
	}

	public List<InitiativeJpaEntity> findInitiativeBySdtAndEdtAndUserSeq(
		LocalDate monthStDt,
		LocalDate monthEndDt,
		Long userSeq
	) {
		return initiativeJpaRepository.findInitiativeBySdtAndEdtAndUserSeq(monthStDt, monthEndDt, userSeq);
	}

	public List<InitiativeJpaEntity> getCountOfInitiativeToGiveFeedback(Long userSeq) {
		return initiativeJpaRepository.getCountOfInitiativeToGiveFeedback(userSeq);
	}

	public Page<InitiativeJpaEntity> findInitiativeByKeyResultTokenAndUserSeq(
		String keyResultToken,
		Long userSeq,
		Pageable pageable
	) {
		return initiativeQueryDslRepository.findInitiativeByKeyResultTokenAndUserSeq(keyResultToken, userSeq, pageable);
	}

	public Optional<InitiativeJpaEntity> findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(
		String initiativeToken,
		Long requesterSeq
	) {
		return initiativeJpaRepository.findInitiativeForFeedbackByInitiativeTokenAndRequester(
			initiativeToken,
			requesterSeq
		);
	}
}
