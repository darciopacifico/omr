/**
 * 
 */
package br.com.dlp.jazzomr.omr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Transient;

import br.com.dlp.jazzomr.base.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * TODO: CHECAR POSSIBILIDADE DE COLOCAR ESTA ENTIDADE NA MESMA TABELA DE AlternativeInstanceVO
 * 
 * @author darcio
 */
@Entity
@JazzClass(name = "Coordenadas", baseEntity = false)
public class OMRCoordinatesVO extends AbstractEntityVO {

	private static final long serialVersionUID = 4996383560623589800L;

	private static final int GROUP_ALTERNATIVE = 3;
	private static final int GROUP_EXAM = 1;
	private static final int GROUP_QUESTION = 2;
	private static final String REGEX_EQA_KEY = "eqaKey-(\\d+)-(\\d+)-(\\d+)";
	private static final Pattern pattern = Pattern.compile(REGEX_EQA_KEY);


	private Long exam;
	private Integer question; 
	private Integer alternative;
	
	private Integer pagina; 
	
	private Integer x;
	private Integer y;

	private Integer h;
	private Integer w;
	
	/**
	 * 
	 * @param value
	 * @param paginaAtual 
	 * @return
	 */
	public static OMRCoordinatesVO parse(String value, Integer pagina){
			
			//"eqaKey-1-1-2"
			
			Matcher matcher = pattern.matcher(value);
			
			OMRCoordinatesVO omrCoordinatesVO = null;
			if(matcher.matches()){
				String strExam = matcher.group(GROUP_EXAM);
				String strQuest = matcher.group(GROUP_QUESTION);
				String strAlter = matcher.group(GROUP_ALTERNATIVE);
				
				Long exam = Long.parseLong(strExam);
				Integer quest = Integer.parseInt(strQuest);
				Integer alter = Integer.parseInt(strAlter);
				
				omrCoordinatesVO = new OMRCoordinatesVO(exam,quest,alter,pagina);
			}
			
			return omrCoordinatesVO;
	}
	
	/**
	 * @param pagina
	 */
	public OMRCoordinatesVO(Integer pagina) {
		super();
		this.pagina = pagina;
	}
	
	
	

	/**
	 * @param exam
	 * @param question
	 * @param alternative
	 * @param pagina 
	 */
	public OMRCoordinatesVO(Long exam, Integer question, Integer alternative, Integer pagina) {
		super();
		this.exam = exam;
		this.question = question;
		this.alternative = alternative;
		this.pagina = pagina;
	}
	
	/**
	 * @return the pagina
	 */
	public Integer getPagina() {
		return pagina;
	}
	
	/**
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * @return the h
	 */
	public Integer getH() {
		return h;
	}

	/**
	 * @return the w
	 */
	public Integer getW() {
		return w;
	}

	
	
	
	
	
	/**
	 * @return the exam
	 */
	@Transient
	public Long getExam() {
		return exam;
	}
	/**
	 * @return the question
	 */
	@Transient
	public Integer getQuestion() {
		return question;
	}
	/**
	 * @return the alternative
	 */
	@Transient
	public Integer getAlternative() {
		return alternative;
	}

	
	/**
	 * @param alternative the alternative to set
	 */
	public void setAlternative(Integer alternative) {
		this.alternative = alternative;
	}

	/**
	 * @param exam the exam to set
	 */
	public void setExam(Long exam) {
		this.exam = exam;
	}

	/**
	 * @param pagina the pagina to set
	 */
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Integer question) {
		this.question = question;
	}

	
	

	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(Integer h) {
		this.h = h;
	}

	/**
	 * @param w the w to set
	 */
	public void setW(Integer w) {
		this.w = w;
	}


}
