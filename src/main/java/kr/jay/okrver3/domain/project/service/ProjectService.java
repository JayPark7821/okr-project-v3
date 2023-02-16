package kr.jay.okrver3.domain.project.service;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;

public interface ProjectService {
	ProjectInfo registerProject(ProjectMasterSaveDto dto, User user);

	ProjectInfo getProjectInfoBy(String projectToken, User user);

	ProjectTeamMemberInfo inviteTeamMember(String projectToken, User invitedUser, User inviter);
}
