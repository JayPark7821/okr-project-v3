package kr.service.okr.persistence.repository.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.persistence.entity.project.aggregate.initiative.InitiativeJpaEntity;

public interface InitiativeJpaRepository extends JpaRepository<InitiativeJpaEntity, Long> {

	@Query("select i "
		+ "from InitiativeJpaEntity i "
		+ "join fetch i.keyResult k "
		+ "join fetch k.project p "
		+ "join fetch p.teamMember pt "
		+ "join fetch i.teamMember t "
		+ "join fetch t.user u "
		+ "where u.userSeq = :userSeq "
		+ "and i.initiativeToken =:initiativeToken ")
	Optional<InitiativeJpaEntity> findInitiativeByInitiativeTokenAndUserSeq(
		@Param("initiativeToken") String initiativeToken,
		@Param("userSeq") Long userSeq
	);

	@Query("select i "
		+ "from InitiativeJpaEntity i "
		+ "join fetch i.keyResult k "
		+ "join fetch k.project p "
		+ "join fetch p.teamMember t "
		+ "join fetch t.user u "
		+ "where u.userSeq = :requesterSeq "
		+ "and i.initiativeToken =:initiativeToken ")
	Optional<InitiativeJpaEntity> findInitiativeForFeedbackByInitiativeTokenAndRequester(
		@Param("initiativeToken") String initiativeToken,
		@Param("requesterSeq") Long requesterSeq);

	@Query("select i "
		+ "from InitiativeJpaEntity i "
		+ "join fetch i.teamMember t "
		+ "join fetch t.project p "
		+ "join fetch t.user u "
		+ "join p.teamMember pt "
		+ "where i.initiativeToken =:initiativeToken "
		+ "and pt.user.userSeq = :userSeq ")
	Optional<InitiativeJpaEntity> findInitiativeDetailByInitiativeTokenAndUserSeq(
		@Param("initiativeToken") String initiativeToken,
		@Param("userSeq") Long userSeq
	);

	@Query("SELECT i " +
		"FROM InitiativeJpaEntity i " +
		"join i.teamMember t " +
		"join t.user u " +
		"where i.done = false " +
		"and :searchDate between i.startDate and i.endDate " +
		"and u.userSeq =:userSeq")
	List<InitiativeJpaEntity> findInitiativeByDateAndUserSeq(
		@Param("searchDate") LocalDate searchDate,
		@Param("userSeq") Long userSeq
	);

	@Query("SELECT i " +
		"FROM InitiativeJpaEntity i " +
		"join i.teamMember t " +
		"join t.user u " +
		"where i.done = false " +
		"and i.startDate <= :monthEdt " +
		"and i.endDate >= :monthSdt " +
		"and u.userSeq =:userSeq")
	List<InitiativeJpaEntity> findInitiativeBySdtAndEdtAndUserSeq(
		@Param("monthSdt") LocalDate monthSdt,
		@Param("monthEdt") LocalDate monthEdt,
		@Param("userSeq") Long userSeq
	);

	@Query("select i  " +
		"from InitiativeJpaEntity i " +
		"join i.teamMember it " +
		"join i.keyResult k " +
		"join k.project p " +
		"join p.teamMember t " +
		"join t.user u " +
		"where i.done = true " +
		"and u.userSeq =:userSeq " +
		"and it.userSeq != :userSeq " +
		"and i.id not in (select f.initiative.id " +
		"                    from FeedbackJpaEntity f " +
		"                    join f.teamMember fm " +
		"                    join fm.user fu " +
		"                    where fu.userSeq = :userSeq ) ")
	List<InitiativeJpaEntity> getCountOfInitiativeToGiveFeedback(
		@Param("userSeq") Long userSeq
	);
}
