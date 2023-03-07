package kr.jay.okrver3.domain.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectInitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;
import kr.jay.okrver3.domain.user.User;

public interface ProjectService {
	ProjectInfo registerProject(ProjectSaveCommand command, User user, List<User> teamMemberUsers);

	ProjectInfo getProjectInfoBy(String projectToken, User user);

	ProjectTeamMembersInfo inviteTeamMember(String projectToken, User invitedUser, User inviter);

	void validateUserToInvite(String projectToken, String invitedUserEmail, User user);

	Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, User user);

	ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, User user);

	String registerKeyResult(ProjectKeyResultSaveCommand command, User user);

	String registerInitiative(ProjectInitiativeSaveCommand command, User user);

	String initiativeFinished(String initiativeToken, User user);

	Page<ProjectInitiativeInfo> getInitiativeByKeyResultToken(String keyResultToken, User user, Pageable pageable);

	String registerFeedback(FeedbackSaveCommand command, User requester);
}

