package kr.jay.okrver3.domain.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.info.ParticipateProjectInfo;

public interface ProjectRepository {
	Project save(Project project);

	Optional<Project> findByProjectTokenAndUser(String projectToken, Long userSeq);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, Long inviterSeq);

	Page<Project> getDetailProjectList(ProjectDetailRetrieveCommand command, Long userSeq);

	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, Long userSeq);

	Optional<Project> findProjectKeyResultByProjectTokenAndUser(String projectToken, Long userSeq);

	Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, Long userSeq);

	double getProjectProgress(Long projectId);

	Optional<Project> findProjectForUpdateById(Long projectId);

	List<Project> findParticipateProjectByUserSeq(Long userSeq);
}
