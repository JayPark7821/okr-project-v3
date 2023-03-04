package kr.jay.okrver3.infrastructure.initiative;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

public interface InitiativeJpaRepository extends JpaRepository<Initiative, Long> {

	@Query("select i "
		+ "from Initiative i "
		+ "join fetch i.keyResult k "
		+ "join fetch k.project p "
		+ "join i.teamMember t "
		+ "where t.user = :user "
		+ "and i.initiativeToken =:initiativeToken ")
	Optional<Initiative> findProjectInitiativeByInitiativeTokenAndUser(
		@Param("initiativeToken") String initiativeToken,
		@Param("user") User user
	);
}
