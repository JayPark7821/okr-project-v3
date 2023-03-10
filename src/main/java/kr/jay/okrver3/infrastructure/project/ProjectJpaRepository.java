package kr.jay.okrver3.infrastructure.project;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.jay.okrver3.domain.project.Project;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

	@Query("select p "
		+ "from Project p "
		+ "join p.teamMember t "
		+ "where t.user.userSeq = :userSeq "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findByProjectTokenAndUser(@Param("projectToken") String projectToken,
		@Param("userSeq") Long userSeq);

	@Query("select p "
		+ "from Project p "
		+ "join fetch p.teamMember pt "
		+ "join fetch pt.user u "
		+ "join p.teamMember t "
		+ "where t.user.userSeq = :userSeq "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(
		@Param("projectToken") String projectToken,
		@Param("userSeq") Long userSeq
	);

	@Query("select p " +
		"from Project p " +
		"join fetch p.teamMember t " +
		"join fetch t.user u " +
		"join p.teamMember pt " +
		"where p.projectToken =:token " +
		"and pt.user.userSeq =:userSeq ")
	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(
		@Param("token") String token,
		@Param("userSeq") Long userSeq
	);

	@Query("select p "
		+ "from Project p "
		+ "join p.teamMember t "
		+ "left join fetch p.keyResults k "
		+ "where t.user.userSeq = :userSeq "
		+ "and p.projectToken =:projectToken ")
	Optional<Project> findProjectKeyResultByProjectTokenAndUser(
		@Param("projectToken") String projectToken,
		@Param("userSeq") Long userSeq
	);

	@Query("select p "
		+ "from Project p "
		+ "join fetch p.teamMember t "
		+ "join fetch t.user u "
		+ "join p.keyResults k "
		+ "where u.userSeq = :userSeq "
		+ "and k.keyResultToken =:keyResultToken ")
	Optional<Project> findByKeyResultTokenAndUser(
		@Param("keyResultToken") String keyResultToken,
		@Param("userSeq") Long userSeq
	);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Project> findProjectForUpdateById(Long projectId);

}
