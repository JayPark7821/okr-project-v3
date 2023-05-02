package kr.service.okr.application.project;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.RegisterInitiativeInfo;
import kr.service.okr.project.usecase.RegisterInitiativeUseCase;
import kr.service.okr.project.usecase.UpdateProjectProgressUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitiativeFacade {

	private final RegisterInitiativeUseCase registerInitiativeUseCase;
	private final UpdateProjectProgressUseCase updateProjectProgressUseCase;

	public String registerInitiative(final RegisterInitiativeCommand command, final Long userSeq) {
		final RegisterInitiativeInfo registerInitiativeInfo =
			registerInitiativeUseCase.command(toCommand(command, userSeq));
		updateProjectProgressUseCase.command(registerInitiativeInfo.projectId());

		return registerInitiativeInfo.initiativeToken();
	}

	private RegisterInitiativeUseCase.Command toCommand(
		final RegisterInitiativeCommand command,
		final Long userSeq
	) {
		return new RegisterInitiativeUseCase.Command(
			command.keyResultToken(),
			command.name(),
			command.startDate(),
			command.endDate(),
			command.detail(),
			userSeq
		);
	}
}
