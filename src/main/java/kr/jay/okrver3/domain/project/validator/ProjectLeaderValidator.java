package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

@Component
public class ProjectLeaderValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_LEADER == type;
	}

	@Override
	public void validate(Project project, User user) {
		if(!project.getProjectLeader().getUser().equals(user))
			throw new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER);
	}

}
