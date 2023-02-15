package kr.jay.okrver3.domain.project.service;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;

public interface ProjectService {
	ProjectInfo registerProject(ProjectMasterSaveDto dto);

	ProjectInfo getProjectInfoBy(String projectToken, User user);
}