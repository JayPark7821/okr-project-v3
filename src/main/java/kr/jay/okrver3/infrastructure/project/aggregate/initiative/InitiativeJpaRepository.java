package kr.jay.okrver3.infrastructure.project.aggregate.initiative;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;

public interface InitiativeJpaRepository extends JpaRepository<Initiative, Long> {

	@Query("select i "
		+ "from Initiative i "
		+ "join fetch i.keyResult k "
		+ "join fetch k.project p "
		+ "join i.teamMember t "
		+ "where t.user.userSeq = :userSeq "
		+ "and i.initiativeToken =:initiativeToken ")
	Optional<Initiative> findInitiativeByInitiativeTokenAndUserSeq(
		@Param("initiativeToken") String initiativeToken,
		@Param("userSeq") Long userSeq
	);

	@Query("select i "
		+ "from Initiative i "
		+ "join fetch i.keyResult k "
		+ "join fetch k.project p "
		+ "join fetch p.teamMember t "
		+ "join fetch t.user u "
		+ "where u.userSeq = :requesterSeq "
		+ "and i.initiativeToken =:initiativeToken ")
	Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequester(
		@Param("initiativeToken") String initiativeToken,
		@Param("requesterSeq") Long requesterSeq);
}
