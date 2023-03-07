package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;
import kr.jay.okrver3.domain.user.User;

@Component
public class SelfFeedbackValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_SELF_FEEDBACK == type;
	}

	@Override
	public void validate(Object... args) {

		TeamMember teamMember = ClassUtils.getSafeCastInstance(args, TeamMember.class);
		User requester = ClassUtils.getSafeCastInstance(args, User.class);

		if (requester.equals(teamMember.getUser()))
			throw new OkrApplicationException(ErrorCode.MOT_AVAIL_FEEDBACK_SELF);
	}

}
