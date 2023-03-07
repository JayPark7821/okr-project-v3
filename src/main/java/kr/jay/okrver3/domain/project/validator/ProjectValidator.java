package kr.jay.okrver3.domain.project.validator;

public interface ProjectValidator {

	boolean support(ProjectValidatorType type);

	void validate(Object... args);
}
