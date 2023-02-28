package kr.jay.okrver3.domain.project.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;

public interface ProjectService {
	ProjectInfo registerProject(ProjectMasterSaveDto dto, User user, List<User> teamMemberUsers);

	ProjectInfo getProjectInfoBy(String projectToken, User user);

	ProjectTeamMemberInfo inviteTeamMember(String projectToken, User invitedUser, User inviter);

	void validateUserToInvite(String projectToken, String invitedUserEmail, User user);

	Page<ProjectDetailInfo> getDetailProjectList(SortType sortType, ProjectType projectType, String validateIncludeFinishedProjectYN, User user, Pageable pageable);
}

