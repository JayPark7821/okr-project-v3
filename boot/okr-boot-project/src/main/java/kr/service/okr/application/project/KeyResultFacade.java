package kr.service.okr.application.project;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.RegisterKeyResultUseCase;
import kr.service.okr.project.usecase.UpdateKeyResultUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeyResultFacade {

	private final RegisterKeyResultUseCase registerKeyResultUseCase;
	private final UpdateKeyResultUseCase updateKeyResultUseCase;

	public String registerKeyResult(RegisterKeyResultCommand requestCommand, final Long userSeq) {
		return registerKeyResultUseCase.command(toCommand(requestCommand, userSeq));
	}

	public void updateKeyResult(final UpdateKeyResultCommand command, final Long userSeq) {
		updateKeyResultUseCase.command(toCommand(command, userSeq));
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

	private UpdateKeyResultUseCase.Command toCommand(
		final UpdateKeyResultCommand requestCommand,
		final Long userSeq
	) {
		return new UpdateKeyResultUseCase.Command(
			requestCommand.keyResultToken(),
			requestCommand.keyResultName(),
			userSeq
		);
	}

}
