package kr.jay.okrver3.infrastructure.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.List;
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

	@Query("select i "
		+ "from Initiative i "
		+ "join fetch i.teamMember t "
		+ "join fetch t.project p "
		+ "join fetch t.user u "
		+ "join p.teamMember pt "
		+ "where i.initiativeToken =:initiativeToken "
		+ "and pt.user.userSeq = :userSeq ")
	Optional<Initiative> findInitiativeDetailByInitiativeTokenAndUserSeq(
		@Param("initiativeToken") String initiativeToken,
		@Param("userSeq") Long userSeq
	);


	@Query("SELECT i " +
		"FROM Initiative i " +
		"join i.teamMember t " +
		"join t.user u " +
		"where i.done = false " +
		"and :searchDate between i.sdt and i.edt " +
		"and u.userSeq =:userSeq")
	List<Initiative> findInitiativeByDateAndUserSeq(
		@Param("searchDate") LocalDate searchDate,
		@Param("userSeq")Long userSeq
	);
}
