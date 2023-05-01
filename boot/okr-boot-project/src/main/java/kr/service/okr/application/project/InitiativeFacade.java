package kr.service.okr.application.project;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.RegisterInitiativeUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitiativeFacade {

	private final RegisterInitiativeUseCase registerInitiativeUseCase;

	public String registerInitiative(final RegisterInitiativeCommand command, final Long userSeq) {
		return registerInitiativeUseCase.command(toCommand(command, userSeq));
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
