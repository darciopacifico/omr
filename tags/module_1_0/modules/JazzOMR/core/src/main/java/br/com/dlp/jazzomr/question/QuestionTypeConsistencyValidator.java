package br.com.dlp.jazzomr.question;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;

import br.com.dlp.framework.vo.ISortable;


public class QuestionTypeConsistencyValidator implements ConstraintValidator<CheckQuestionTypeConsistency, QuestionVO> {

	public void initialize(CheckQuestionTypeConsistency constraintAnnotation) {
	}

	
	/**
	 * 
	 */
	public boolean isValid(QuestionVO questionVO, ConstraintValidatorContext constraintContext) {
		
		constraintContext.disableDefaultConstraintViolation();

		List<DissertativeVO> 	dissertatives 	= questionVO.getDissertativeVOs();
		List<AlternativeVO> 	alternatives 		= questionVO.getAlternativeVOs();

		if(CollectionUtils.isEmpty(dissertatives) && CollectionUtils.isEmpty(alternatives)){
			constraintContext.buildConstraintViolationWithTemplate("Uma questão deve possuir ao menos duas alternativas ou um critério dissertativo!").addNode("criterionVOs").addConstraintViolation();
			return false;
		}
		
		Boolean alternativeResult = validateAlternativeType(questionVO, constraintContext);
		
		//para um resultado final positivo os dois tipos de criterios tem q estar OK
		return alternativeResult;
	}


	/**
	 * Consiste os atributor minimos de uma questao alternativa
	 * @param questionVO
	 * @param constraintContext
	 * @return
	 */
	protected boolean validateAlternativeType(QuestionVO questionVO, ConstraintValidatorContext constraintContext) {
		
		Collection<AlternativeVO> criterions = questionVO.getAlternativeVOs();
		
		if(CollectionUtils.isEmpty(criterions)){
			//nao há o que validar....
			return true;
		}
		
		if(criterions.size()==1){
			constraintContext.buildConstraintViolationWithTemplate("Uma questão deve possuir ao menos duas alternativas.").addNode("alternatives").addConstraintViolation();
			return false;
			
		}
			
			
		Integer correctAlternativeScore = questionVO.countCorrectScore();
		Integer totalAlternative = criterions.size();
		
		return consistAlternatives(questionVO, constraintContext, correctAlternativeScore, correctAlternativeScore, totalAlternative);
	
	}

	/**
	 * Consiste as quantidades de alternativas corretas, totais e esperado. 
	 * @param questionVO
	 * @param constraintContext
	 * @param correctExpectedAlternative
	 * @param scoreCorrect
	 * @param totalAlt
	 * @return
	 */
	protected boolean consistAlternatives(ISortable questionVO, ConstraintValidatorContext constraintContext, 
			Integer expected,
			Integer scoreCorrect,
			Integer totalAlt) {

		if(!(expected>0)){
			
			constraintContext.buildConstraintViolationWithTemplate("A questão deve esperar ao menos uma alternativa correta.").addNode("correctAlternatives").addConstraintViolation();
			return false;
		}
		
		
		if(!(scoreCorrect>=1)){
			
			constraintContext.buildConstraintViolationWithTemplate("Ao menos uma alternativa deve ser correta.").addNode("correctAlternatives").addConstraintViolation();
			return false;
			
		}else if(!(totalAlt>scoreCorrect)){
			
			constraintContext.buildConstraintViolationWithTemplate("Ao menos uma alternativa deve ser incorreta.").addNode("correctAlternatives").addConstraintViolation();
			return false;
			
		}
		
			
		
		return true;
	}



}
