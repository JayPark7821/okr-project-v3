package kr.jay.okrver3.domain.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.feedback.Feedback;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.user.User;

public interface ProjectRepository {
	Project save(Project project);

	Optional<Project> findByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, User inviter);

	Page<Project> getDetailProjectList(ProjectDetailRetrieveCommand command, User user);

	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findProjectKeyResultByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, User user);

	double getProjectProgress(Long projectId);

	Optional<Initiative> findProjectInitiativeByInitiativeTokenAndUser(String initiativeToken, User user);

	Project getReferenceById(Long projectId);

	Optional<Project> findProjectForUpdateById(Long projectId);

	Page<Initiative> findInitiativeByKeyResultTokenAndUser(String keyResultToken, User user, Pageable pageable);

	Initiative saveAndFlushInitiative(Initiative initiative);

	Optional<Initiative> findInitiativeForFeedbackByInitiativeTokenAndRequester(String initiativeToken, User requester);

	Feedback saveFeedback(Feedback feedback);
}
