package kr.service.okr.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;

public class DateValidator implements ConstraintValidator<DateValid, String> {

	private static final Logger log = LoggerFactory.getLogger(DateValidator.class);
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