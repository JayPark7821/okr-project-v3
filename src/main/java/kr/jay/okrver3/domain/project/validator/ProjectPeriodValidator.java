package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

@Component
public class ProjectPeriodValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_PROJECT_PERIOD == type;
	}

	@Override
	public void validate(Project project, User user) {
		if(!project.isValidUntilToday())
			throw new OkrApplicationException(ErrorCode.NOT_UNDER_PROJECT_DURATION);
	}

}
