package kr.jay.okrver3.interfaces.project;

import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.user.User;

public record ProjectDetailRetrieveCommand(SortType sortType, ProjectType projectType, String includeFinishedProjectYN,
										   User user, Pageable pageable) {
}
