package kr.jay.okrver3.domain.project.aggregate.initiative;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.user.User;

public interface InitiativeRepository {

	Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequester(String initiativeToken, User requester);

	Initiative saveAndFlush(Initiative initiative);

	Page<Initiative> findInitiativeByKeyResultTokenAndUser(String keyResultToken, User user, Pageable pageable);

	Optional<Initiative> findProjectInitiativeByInitiativeTokenAndUser(String initiativeToken, User user);

}
