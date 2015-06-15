package br.com.dlp.jazzomr.question;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QuestionTypeConsistencyValidator.class)
public @interface CheckQuestionTypeConsistency {
	String message() default "{br.com.dlp.jazzomr.question.typeconsistency}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
