package kr.service.okr.domain.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InitiativeRepository {

	Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(String initiativeToken,
		Long requesterSeq);

	Initiative saveAndFlush(Initiative initiative);

	Page<Initiative> findInitiativeByKeyResultTokenAndUserSeq(String keyResultToken, Long userSeq, Pageable pageable);

	Optional<Initiative> findInitiativeByInitiativeTokenAndUserSeq(String initiativeToken, Long userSeq);

	Optional<Initiative> findInitiativeDetailByInitiativeTokenAndUserSeq(String initiativeToken, Long userSeq);

	List<Initiative> findInitiativeByDate(LocalDate searchDate, Long userSeq);

	List<Initiative> findInitiativeBySdtAndEdtAndUserSeq(LocalDate monthStDt, LocalDate monthEndDt, Long userSeq);

	List<Initiative> getCountOfInitiativeToGiveFeedback(Long userSeq);

}
