package kr.jay.okrver3.domain.project.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.service.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.service.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.service.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.service.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.service.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.service.info.ProjectInfo;
import kr.jay.okrver3.domain.project.service.info.ProjectInitiativeInfo;
import kr.jay.okrver3.domain.project.service.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.service.info.ProjectTeamMembersInfo;
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

	ProjectInitiativeInfo getProjectInitiativeInfoByInitiativeTokenAndUser(String initiativeToken, User requester);
}

