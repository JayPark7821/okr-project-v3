package kr.service.okr.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.ClassUtils;
import kr.service.okr.domain.project.Project;

@Component
public class ProjectKeyResultCountValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_KEYRESULT_COUNT == type;
	}

	@Override
	public void validate(Object... args) {

		Project project = ClassUtils.getSafeCastInstance(args, Project.class);
		
		if (!project.isKeyResultAddable())
			throw new OkrApplicationException(ErrorCode.KEYRESULT_LIMIT_EXCEED);
	}

}
