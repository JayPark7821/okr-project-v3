package kr.service.okr.domain.project.validator;

public interface ProjectValidator {

	boolean support(ProjectValidatorType type);

	void validate(Object... args);
}
