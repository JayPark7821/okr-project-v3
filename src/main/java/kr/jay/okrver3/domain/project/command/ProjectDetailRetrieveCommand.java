package kr.jay.okrver3.domain.project.command;

import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;

public record ProjectDetailRetrieveCommand(SortType sortType, ProjectType projectType, String includeFinishedProjectYN,
										   Pageable pageable) {
}
