package kr.jay.okrver3.application.project;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	public String registerProject(ProjectMasterSaveDto dto, User user) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
