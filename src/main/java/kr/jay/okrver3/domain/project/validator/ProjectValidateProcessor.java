package kr.jay.okrver3.domain.project.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProjectValidateProcessor {

	private final List<ProjectValidator> projectValidatorList;

	public void validate(ProjectValidateProcessorType type, Project project, User user) {
		List<ProjectValidatorType> projectValidatorTypes = type.getProjectValidatorTypes();
		projectValidatorTypes.forEach(validator -> {
			ProjectValidator projectValidator = routingValidatorCaller(validator);
			projectValidator.validate(project, user);
		});
	}

	private ProjectValidator routingValidatorCaller(ProjectValidatorType type) {
			return projectValidatorList.stream()
				.filter(projectValidator -> projectValidator.support(type))
				.findFirst()
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.UNSUPPORTED_VALIDATOR));
	}
}
