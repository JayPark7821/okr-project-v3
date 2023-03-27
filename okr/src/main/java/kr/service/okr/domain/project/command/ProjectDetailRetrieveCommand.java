package kr.service.okr.domain.project.command;

import org.springframework.data.domain.Pageable;

import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.SortType;

public record ProjectDetailRetrieveCommand(SortType sortType, ProjectType projectType, String includeFinishedProjectYN,
										   Pageable pageable) {
}
