package kr.jay.okrver3.domain.project.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProjectValidateProcessor {

	private final List<ProjectValidator> projectValidatorList;

	public void validate(List<ProjectValidatorType> type, Object... args) {
		type.forEach(validatorType -> {
			ProjectValidator projectValidator = routingValidatorCaller(validatorType);
			projectValidator.validate(args);
		});
	}

	private ProjectValidator routingValidatorCaller(ProjectValidatorType type) {
		return projectValidatorList.stream()
			.filter(projectValidator -> projectValidator.support(type))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.UNSUPPORTED_VALIDATOR));
	}
}
