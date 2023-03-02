package kr.jay.okrver3.infrastructure.project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

	@Query("select p "
		+ "from Project p "
		+ "join p.teamMember t "
		+ "where t.user = :user "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findByProjectTokenAndUser(@Param("projectToken") String projectToken, @Param("user") User user);

	@Query("select p "
		+ "from Project p "
		+ "join fetch p.teamMember pt "
		+ "join fetch pt.user u "
		+ "join p.teamMember t "
		+ "where t.user = :user "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(@Param("projectToken") String projectToken,
		@Param("user") User user);

	@Query("select p " +
		"from Project p " +
		"join fetch p.teamMember t " +
		"join fetch t.user u " +
		"join p.teamMember pt " +
		"where p.projectToken =:token " +
		"and pt.user =:user ")
	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(
		@Param("token") String token,
		@Param("user") User user
	);

	@Query("select p "
		+ "from Project p "
		+ "join p.teamMember t "
		+ "left join fetch p.keyResults k "
		+ "where t.user = :user "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findProjectKeyResultByProjectTokenAndUser(@Param("projectToken") String projectToken, @Param("user") User user);

	@Query("select p "
		+ "from Project p "
		+ "join p.teamMember t "
		+ "join fetch p.keyResults k "
		+ "where t.user = :user "
		+ "and k.keyResultToken =:keyResultToken ")
	Optional<Project> findByKeyResultTokenAndUser(
		@Param("keyResultToken") String keyResultToken,
		@Param("user") User user);
}
