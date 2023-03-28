package kr.service.okrcommon.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;

public class DateValidator implements ConstraintValidator<DateValid, String> {

	private String pattern;

	@Override
	public void initialize(DateValid constraintAnnotation) {
		this.pattern = constraintAnnotation.pattern();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		try {
			if (value == null) {
				throw new OkrApplicationException(ErrorCode.REQUIRED_DATE_VALUE);
			}
			LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern(this.pattern)));
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}
}