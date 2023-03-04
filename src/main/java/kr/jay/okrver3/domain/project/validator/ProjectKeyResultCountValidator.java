package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;

@Component
public class ProjectKeyResultCountValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_KEYRESULT_COUNT == type;
	}

	@Override
	public void validate(Project project, Object object) {
		if (!project.isKeyResultAddable())
			throw new OkrApplicationException(ErrorCode.KEYRESULT_LIMIT_EXCEED);
	}

}
