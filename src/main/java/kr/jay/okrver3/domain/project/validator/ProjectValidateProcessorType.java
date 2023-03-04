package kr.jay.okrver3.domain.project.validator;

import static kr.jay.okrver3.domain.project.validator.ProjectValidatorType.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProjectValidateProcessorType {

	PROJECT_BASIC_VALIDATION(List.of(VALIDATE_LEADER, VALIDATE_PROJECT_PERIOD)),
	ADD_KEYRESULT_VALIDATION(List.of(VALIDATE_LEADER, VALIDATE_PROJECT_PERIOD, VALIDATE_KEYRESULT_COUNT)),
	ADD_INITIATIVE_VALIDATION(List.of(VALIDATE_PROJECT_PERIOD, VALIDATE_PROJECT_INITIATIVE_DATE)),
	INITIATIVE_FINISH_VALIDATION(List.of(VALIDATE_PROJECT_PERIOD, VALIDATE_INITIATIVE_DONE));
	final List<ProjectValidatorType> projectValidatorTypes;

}
