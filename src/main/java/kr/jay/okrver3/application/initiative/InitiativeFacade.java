package kr.jay.okrver3.application.initiative;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.initiative.service.InitiativeService;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiativeFacade {

	private final ProjectService projectService;
	private final InitiativeService initiativeService;

	public String registerInitiative(InitiativeSaveCommand command, User user) {
		ProjectInfo projectInfo = projectService.getKeyResultIdForSavingInitiative(command.keyResultToken(), user);
		initiativeService.registerInitiative(command, , user);
		return null;
	}
}
