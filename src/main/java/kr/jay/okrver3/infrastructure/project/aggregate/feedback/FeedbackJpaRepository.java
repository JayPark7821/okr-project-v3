package kr.jay.okrver3.infrastructure.project.aggregate.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;

public interface FeedbackJpaRepository extends JpaRepository<Feedback, Long> {

	@Query("SELECT f " +
		"FROM Feedback f " +
		"join fetch f.teamMember t " +
		"join fetch t.user u " +
		"join fetch f.initiative i " +
		"where f.initiative.initiativeToken =:initiativeToken " +
		"order by f.createdDate desc")
	List<Feedback> findInitiativeFeedbacksByInitiativeToken(
		@Param("initiativeToken") String initiativeToken
	);
}
