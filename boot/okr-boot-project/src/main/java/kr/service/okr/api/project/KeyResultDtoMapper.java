package kr.service.okr.api.project;

import kr.service.okr.application.project.RegisterKeyResultCommand;
import kr.service.okr.project.api.RegisterKeyResultRequest;

public class KeyResultDtoMapper {

	public static RegisterKeyResultCommand toCommand(final RegisterKeyResultRequest request) {
		return new RegisterKeyResultCommand(request.projectToken(), request.keyResultName());
	}
}
