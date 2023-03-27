package kr.service.okr.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			log.error("DateValidator : {}", e);
			return false;
		}
		return true;
	}
}