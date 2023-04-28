package kr.service.okr.application.project;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.RegisterKeyResultUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeyResultFacade {

	private final RegisterKeyResultUseCase registerKeyResultUseCase;

	public String registerKeyResult(RegisterKeyResultCommand requestCommand, final Long userSeq) {
		return registerKeyResultUseCase.command(toCommand(requestCommand, userSeq));
	}

	private RegisterKeyResultUseCase.Command toCommand(
		final RegisterKeyResultCommand requestCommand,
		final Long userSeq
	) {
		return new RegisterKeyResultUseCase.Command(
			requestCommand.projectToken(),
			requestCommand.keyResultName(),
			userSeq
		);
	}
}
