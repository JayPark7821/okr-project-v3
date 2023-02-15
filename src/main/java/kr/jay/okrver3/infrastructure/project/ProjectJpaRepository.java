package kr.jay.okrver3.infrastructure.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.user.User;

public interface ProjectJpaRepository extends ProjectRepository, JpaRepository<Project, Long> {

	@Query("select p "
		+ "from Project p "
		+ "join p.teamMember t "
		+ "where t.user = :user "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findByProjectTokenAndUser(@Param("projectToken") String projectToken, @Param("user") User user);

	@Query("select p "
		+ "from Project p "
		+ "join fetch p.teamMember t "
		+ "where t.user = :user "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(@Param("projectToken") String projectToken,
		@Param("user") User user);
}
