package kr.jay.okrver3.domain.project.validator;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

public interface ProjectValidator {

	boolean support(ProjectValidatorType type);

	void validate(Project project, User user);
}
