package kr.jay.okrver3.domain.project.validator;

import kr.jay.okrver3.domain.project.Project;

public interface ProjectValidator {

	boolean support(ProjectValidatorType type);

	void validate(Project project, Object object);
}
