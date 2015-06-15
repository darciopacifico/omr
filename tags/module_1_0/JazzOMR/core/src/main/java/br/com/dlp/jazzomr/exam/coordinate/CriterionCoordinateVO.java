/**
 * 
 */
package br.com.dlp.jazzomr.exam.coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import br.com.dlp.jazzomr.base.AbstractEntityVO;
import br.com.dlp.jazzomr.omr.OMRCoordinatesVO;
import br.com.dlp.jazzomr.person.EAuthority;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(name="Coordenadas das Criterios que Questoes")
public class CriterionCoordinateVO extends AbstractEntityVO<Long> implements IOMRMarkable {


	private static final long serialVersionUID = -6732312696553177460L;

	private AbstractCriterionVO abstractCriterionVO;
	
	private Integer alternativeOrder;

	private static final int GROUP_ALTERNATIVE = 3;
	private static final int GROUP_EXAM = 1;
	private static final int GROUP_QUESTION = 2;
	private static final String REGEX_EQA_KEY = "eqaKey-(\\d+)-(\\d+)-(\\d+)";
	private static final Pattern pattern = Pattern.compile(REGEX_EQA_KEY);

	private Integer pagina; 

	private List<Integer> paginas = new ArrayList<Integer>(2); 

	
	private Integer x;
	private Integer y;

	private Integer h;
	private Integer w;
	

	private QuestionVO questionVO;
	
	private Integer questionOrder;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	
	/**
	 * @return the questionVO
	 */
	@ManyToOne
	public QuestionVO getQuestionVO() {
		return questionVO;
	}
	
	
	/**
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
	 * @return the AbstractCriterionVO
	 */
	@ManyToOne
	public AbstractCriterionVO getAbstractCriterionVO() {
		return abstractCriterionVO;
	}
	
	/**
	 * @return the pagina
	 */
	@ElementCollection(targetClass=Integer.class)
	@JoinTable(name = "criterion_coordinate_pages")
	public List<Integer> getPaginas() {
		return paginas;
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
	 * @param pagina the pagina to set
	 */
	public void setPaginas(List<Integer> paginas) {
		this.paginas = paginas;
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



	
	public CriterionCoordinateVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pk
	 */
	public CriterionCoordinateVO(Long pk) {
		super(pk);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the alternativeOrder
	 */
	public Integer getAlternativeOrder() {
		return alternativeOrder;
	}

	/**
	 * @param alternativeOrder the alternativeOrder to set
	 */
	public void setAlternativeOrder(Integer ordem) {
		this.alternativeOrder = ordem;
	}
	/**
	 * @param AbstractCriterionVO the AbstractCriterionVO to set
	 */
	public void setAbstractCriterionVO(AbstractCriterionVO abstractCriterionVO) {
		this.abstractCriterionVO = abstractCriterionVO;
	}



	/**
	 * @param wCoef
	 * @param intValue
	 * @return
	 */
	protected int applyAdjFactor(double wCoef, Integer intValue) {
		return (int) (((double)intValue)/wCoef);
	}


	public int getX(double wCoef) {
		return  applyAdjFactor(wCoef, getX());
	}
	
	public int getY(double hCoef) {
		return applyAdjFactor(hCoef, getY());
	}

	public int getW(double wCoef) {
		return applyAdjFactor(wCoef, getW());
	}

	public int getH(double hCoef) {
		return applyAdjFactor(hCoef, getH());
	}


	public void setQuestionVO(QuestionVO questionVO) {
		this.questionVO = questionVO;
	}


	public Integer getQuestionOrder() {
		return questionOrder;
	}


	public void setQuestionOrder(Integer questionOrder) {
		this.questionOrder = questionOrder;
	}



	public Integer getPagina() {
		return pagina;
	}



	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

}
