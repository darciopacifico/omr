package br.com.dlp.jazz.omr.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import br.com.dlp.jazzomr.question.QuestionVO;

public class TestDomainValidations {

	public static void main(String[] args) {
		
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		QuestionVO questionVO = new QuestionVO();
		
		questionVO.setDescription("zubalele");
		
		Set<ConstraintViolation<QuestionVO>> a = validator.validate(questionVO);

		for (ConstraintViolation<QuestionVO> constraintViolation : a) {
			System.out.println(constraintViolation);
		}
		
	}
	
}
