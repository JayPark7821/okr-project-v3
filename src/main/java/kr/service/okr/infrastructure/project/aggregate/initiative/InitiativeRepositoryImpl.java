package kr.service.okr.infrastructure.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.service.okr.domain.project.aggregate.initiative.Initiative;
import kr.service.okr.domain.project.aggregate.initiative.InitiativeRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InitiativeRepositoryImpl implements InitiativeRepository {

	private final InitiativeJpaRepository initiativeJpaRepository;
	private final InitiativeQueryDslRepository initiativeQueryDslRepository;

	@Override
	public Optional<Initiative> findInitiativeByInitiativeTokenAndUserSeq(String initiativeToken, Long userSeq) {
		return initiativeJpaRepository.findInitiativeByInitiativeTokenAndUserSeq(initiativeToken, userSeq);
	}

	@Override
	public Optional<Initiative> findInitiativeDetailByInitiativeTokenAndUserSeq(String initiativeToken, Long userSeq) {
		return initiativeJpaRepository.findInitiativeDetailByInitiativeTokenAndUserSeq(initiativeToken, userSeq);
	}

	@Override
	public List<Initiative> findInitiativeByDate(LocalDate searchDate, Long userSeq) {
		return initiativeJpaRepository.findInitiativeByDateAndUserSeq(searchDate, userSeq);
	}

	@Override
	public List<Initiative> findInitiativeBySdtAndEdtAndUserSeq(
		LocalDate monthStDt,
		LocalDate monthEndDt,
		Long userSeq
	) {
		return initiativeJpaRepository.findInitiativeBySdtAndEdtAndUserSeq(monthStDt, monthEndDt, userSeq);
	}

	@Override
	public List<Initiative> getCountOfInitiativeToGiveFeedback(Long userSeq) {
		return initiativeJpaRepository.getCountOfInitiativeToGiveFeedback(userSeq);
	}

	@Override
	public Page<Initiative> findInitiativeByKeyResultTokenAndUserSeq(
		String keyResultToken,
		Long userSeq,
		Pageable pageable
	) {
		return initiativeQueryDslRepository.findInitiativeByKeyResultTokenAndUserSeq(keyResultToken, userSeq, pageable);
	}

	@Override
	public Initiative saveAndFlush(Initiative initiative) {
		return initiativeJpaRepository.saveAndFlush(initiative);
	}

	@Override
	public Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(
		String initiativeToken,
		Long requesterSeq
	) {
		return initiativeJpaRepository.findInitiativeForFeedbackByInitiativeTokenAndRequester(
			initiativeToken,
			requesterSeq
		);
	}

}
