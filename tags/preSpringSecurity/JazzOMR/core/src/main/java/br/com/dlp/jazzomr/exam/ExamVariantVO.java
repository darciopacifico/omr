/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(name="Variação Exame")
@FetchProfile(name="examvariant_exam",fetchOverrides={
		@FetchProfile.FetchOverride(mode=FetchMode.JOIN, association = "examVO", entity = ExamVariantVO.class)
})	
public class ExamVariantVO extends AbstractLogEntityVO  {

	private static final long serialVersionUID = -7437674801413339846L;
	
	public ExamVariantVO() {}
	
	private ExamVO examVO;
	private EnumerationModel questionEnumModel=EnumerationModel.NUMERIC;
	private EnumerationModel alternativeEnumModel=EnumerationModel.ALFABETIC;

	private List<CriterionCoordinateVO> criterionCoordinates = new ArrayList<CriterionCoordinateVO>(20);
	
	/**
	 * @return the exam
	 */
	@JazzProp(name = "Exame Origem")
	@ManyToOne
	public ExamVO getExamVO() {
		return examVO;
	}
	
	
	/**
	 * @return the questionCoordinates
	 */
	@JazzProp(name="Coordenadas das Questões",renderer=JazzRenderer.GRID)
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="fk_exam_variant")
	public List<CriterionCoordinateVO> getCriterionCoordinates() {
		return criterionCoordinates;
	}
		
	
	/**
	 * @param exam the exam to set
	 */
	public void setExamVO(ExamVO exam) {
		this.examVO = exam;
	}



	/**
	 * @return the questionEnumModel
	 */
	public EnumerationModel getQuestionEnumModel() {
		return questionEnumModel;
	}


	/**
	 * @return the alternativeEnumModel
	 */
	public EnumerationModel getAlternativeEnumModel() {
		return alternativeEnumModel;
	}


	/**
	 * @param questionEnumModel the questionEnumModel to set
	 */
	public void setQuestionEnumModel(EnumerationModel questionEenumModel) {
		this.questionEnumModel = questionEenumModel;
	}


	/**
	 * @param alternativeEnumModel the alternativeEnumModel to set
	 */
	public void setAlternativeEnumModel(EnumerationModel alternativeEenumModel) {
		this.alternativeEnumModel = alternativeEenumModel;
	}


	public void setCriterionCoordinates(List<CriterionCoordinateVO> criterionCoordinates) {
		this.criterionCoordinates = criterionCoordinates;
	}
	
}
