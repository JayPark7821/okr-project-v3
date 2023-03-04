package kr.jay.okrver3.domain.project.service;

import java.util.List;

import org.springframework.data.domain.Page;

import kr.jay.okrver3.application.project.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.interfaces.project.ProjectKeyResultSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectSideMenuResponse;

public interface ProjectService {
	ProjectInfo registerProject(ProjectMasterSaveDto dto, User user, List<User> teamMemberUsers);

	ProjectInfo getProjectInfoBy(String projectToken, User user);

	ProjectTeamMemberInfo inviteTeamMember(String projectToken, User invitedUser, User inviter);

	void validateUserToInvite(String projectToken, String invitedUserEmail, User user);

	Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command);

	ProjectSideMenuResponse getProjectSideMenuDetails(String projectToken, User user);

	String registerKeyResult(ProjectKeyResultSaveDto projectKeyResultSaveDto, User user);

	String registerInitiative(ProjectInitiativeSaveCommand command, User user);

	String initiativeFinished(String initiativeToken, User user);
}

