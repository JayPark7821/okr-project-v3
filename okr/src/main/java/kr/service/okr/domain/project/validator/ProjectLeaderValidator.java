package kr.service.okr.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;
import kr.service.okrcommon.common.utils.ClassUtils;
import kr.service.okr.domain.project.Project;

@Component
public class ProjectLeaderValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_LEADER == type;
	}

	@Override
	public void validate(Object... args) {

		Project project = ClassUtils.getSafeCastInstance(args, Project.class);
		Long userSeq = ClassUtils.getSafeCastInstance(args, Long.class);

		if (!project.getProjectLeader().getUser().getUserSeq().equals(userSeq))
			throw new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER);
	}

}
