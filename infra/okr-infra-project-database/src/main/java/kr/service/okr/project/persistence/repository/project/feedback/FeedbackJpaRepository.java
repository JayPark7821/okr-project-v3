package kr.service.okr.project.persistence.repository.project.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.service.okr.project.persistence.entity.project.feedback.FeedbackJpaEntity;

public interface FeedbackJpaRepository extends JpaRepository<FeedbackJpaEntity, Long> {

	@Query("SELECT f " +
		"FROM FeedbackJpaEntity f " +
		"join fetch f.teamMember t " +
		"join fetch f.initiative i " +
		"where f.initiative.initiativeToken =:initiativeToken " +
		"order by f.createdDate desc")
	List<FeedbackJpaEntity> findInitiativeFeedbacksByInitiativeToken(
		@Param("initiativeToken") String initiativeToken
	);
}
