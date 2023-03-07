package kr.jay.okrver3.infrastructure.project.aggregate.initiative;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.aggregate.initiative.InitiativeRepository;
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
	public Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(String initiativeToken,
		Long requesterSeq) {
		return initiativeJpaRepository.findInitiativeForFeedbackByInitiativeTokenAndRequester(
			initiativeToken,
			requesterSeq
		);
	}

}
