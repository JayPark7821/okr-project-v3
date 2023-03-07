package kr.jay.okrver3.domain.project.aggregate.initiative;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InitiativeRepository {

	Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(String initiativeToken,
		Long requesterSeq);

	Initiative saveAndFlush(Initiative initiative);

	Page<Initiative> findInitiativeByKeyResultTokenAndUserSeq(String keyResultToken, Long userSeq, Pageable pageable);

	Optional<Initiative> findInitiativeByInitiativeTokenAndUserSeq(String initiativeToken, Long userSeq);

}
