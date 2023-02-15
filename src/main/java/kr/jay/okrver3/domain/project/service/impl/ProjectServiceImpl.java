package kr.jay.okrver3.domain.project.service.impl;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	@Override
	public ProjectInfo registerProject(ProjectMasterSaveDto dto) {
		return null;
	}
}
