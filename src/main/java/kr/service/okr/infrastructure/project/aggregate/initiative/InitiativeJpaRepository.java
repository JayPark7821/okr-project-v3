package kr.service.okr.infrastructure.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.domain.project.aggregate.initiative.Initiative;

public interface InitiativeJpaRepository extends JpaRepository<Initiative, Long> {

	@Query("select i "
		+ "from Initiative i "
		+ "join fetch i.keyResult k "
		+ "join fetch k.project p "
		+ "join fetch p.teamMember pt "
		+ "join fetch i.teamMember t "
		+ "join fetch t.user u "
		+ "where u.userSeq = :userSeq "
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

	@Query("SELECT i " +
		"FROM Initiative i " +
		"join i.teamMember t " +
		"join t.user u " +
		"where i.done = false " +
		"and i.sdt <= :monthEdt " +
		"and i.edt >= :monthSdt " +
		"and u.userSeq =:userSeq")
	List<Initiative> findInitiativeBySdtAndEdtAndUserSeq(
		@Param("monthSdt") LocalDate monthSdt,
		@Param("monthEdt") LocalDate monthEdt,
		@Param("userSeq")Long userSeq
	);


	@Query("select i  " +
		"from Initiative i " +
		"join i.teamMember it " +
		"join i.keyResult k " +
		"join k.project p " +
		"join p.teamMember t " +
		"join t.user u " +
		"where i.done = true " +
		"and u.userSeq =:userSeq " +
		"and it.userSeq != :userSeq " +
		"and i.iniId not in (select f.initiative.iniId " +
		"                    from Feedback f " +
		"                    join f.teamMember fm " +
		"                    join fm.user fu " +
		"                    where fu.userSeq = :userSeq ) ")
	List<Initiative> getCountOfInitiativeToGiveFeedback(
		@Param("userSeq")Long userSeq
	);
}
