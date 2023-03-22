package kr.service.okr.common.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValid {

  String message() default "6자리의 yyyyMMdd 형식이어야 합니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String pattern() default "yyyyMMdd";
}