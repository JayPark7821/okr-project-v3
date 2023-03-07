package kr.jay.okrver3.infrastructure.project.aggregate.initiative;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.aggregate.initiative.InitiativeRepository;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InitiativeRepositoryImpl implements InitiativeRepository {

	private final InitiativeJpaRepository initiativeJpaRepository;
	private final InitiativeQueryDslRepository initiativeQueryDslRepository;

	@Override
	public Optional<Initiative> findInitiativeByInitiativeTokenAndUser(String initiativeToken, User user) {
		return initiativeJpaRepository.findInitiativeByInitiativeTokenAndUser(initiativeToken, user);
	}

	@Override
	public Page<Initiative> findInitiativeByKeyResultTokenAndUser(
		String keyResultToken,
		User user,
		Pageable pageable
	) {
		return initiativeQueryDslRepository.findInitiativeByKeyResultTokenAndUser(keyResultToken, user, pageable);
	}

	@Override
	public Initiative saveAndFlush(Initiative initiative) {
		return initiativeJpaRepository.saveAndFlush(initiative);
	}

	@Override
	public Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequester(String initiativeToken,
		User requester) {
		return initiativeJpaRepository.findInitiativeForFeedbackByInitiativeTokenAndRequester(initiativeToken,
			requester);
	}

}
