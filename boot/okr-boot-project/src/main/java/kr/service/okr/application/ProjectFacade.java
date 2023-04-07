package kr.service.okr.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.RegisterProjectUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectFacade {

	private final RegisterProjectUseCase registerProjectUseCase;

	public String registerProject(RegisterProjectCommand requestCommand) {

		final RegisterProjectUseCase.Command command = new RegisterProjectUseCase.Command(
			requestCommand.objective(),
			requestCommand.startDate(),
			requestCommand.endDate(),
			requestCommand.userSeq(),
			List.of()
		);

		return registerProjectUseCase.registerProject(command);
	}
}
