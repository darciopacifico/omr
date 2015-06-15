/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanComparator;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * @author darcio
 * 
 */
@Entity
@JazzClass(name = "Exame")
/*
 * */
@FetchProfiles(value={
		
		@FetchProfile(name="exame_com_questionarios", fetchOverrides={
				@FetchProfile.FetchOverride(entity=ExamVO.class, association="examOMRMetadataVO",	mode=FetchMode.JOIN)
		}),
		
})  

public class ExamVO extends AbstractLogEntityVO  {

	private static final long serialVersionUID = 7124967832372109142L;
	
	private String description;
	private List<QuestionnaireVO> questionnaires = new ArrayList<QuestionnaireVO>(10);
	private String reportResourceAsStream;

	
	private Integer numberOfOMRCornerMarks=2;
	private Map<String, ExamOMRMetadataVO> examOMRMetadataVO = new HashMap<String, ExamOMRMetadataVO>(3);
	protected java.awt.geom.Point2D.Double topLeftCorner;
	protected java.awt.geom.Point2D.Double bottomRightCorner;
	protected Double examRefWidth;
	protected Double examRefHeigth; 
	
	private Integer totalPages=1;
	
	/**
	 * Construtor padrao
	 */
	public ExamVO() {
	}

	/**
	 * @return the questionnaires
	 */
	@JazzProp(name = "Questionários",searchable=false,listable=false)
	@ManyToMany(cascade = CascadeType.DETACH, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	public List<QuestionnaireVO> getQuestionnaires() {
		return questionnaires;
	}
	
	/**
	 * @return the reportResourceAsStream
	 */
	public String getReportResourceAsStream() {
		return reportResourceAsStream;
	}
	
	/**
	 * @return the description
	 */
	@JazzProp(name="Descrição")
	@NotEmpty
	public String getDescription() {
		return description;
	}
	
	
	
	
	
	
	
	
	/**
	 * @return the examOMRMetadataVO
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_exam")
	@MapKey(name = "omrKey")
	@JazzProp(name="",ignore=true)
	@Fetch(FetchMode.SELECT)
	public Map<String, ExamOMRMetadataVO> getExamOMRMetadataVO() {
		return examOMRMetadataVO;
	}

	/**
	 * @param questionnaires
	 *          the questionnaires to set
	 */
	public void setQuestionnaires(List<QuestionnaireVO> questionnaires) {
		this.questionnaires = questionnaires;
	}

	/**
	 * @param reportResourceAsStream
	 *          the reportResourceAsStream to set
	 */
	public void setReportResourceAsStream(String reportResourceAsStream) {
		this.reportResourceAsStream = reportResourceAsStream;
	}

	/**
	 * @param examOMRMetadataVO
	 *          the examOMRMetadataVO to set
	 */
	public void setExamOMRMetadataVO(Map<String, ExamOMRMetadataVO> examOMRMetadataVO) {
		this.examOMRMetadataVO = examOMRMetadataVO;
	}


	/**
	 * @param numberOfOMRCornerMarks
	 *          the numberOfOMRCornerMarks to set
	 */
	public void setNumberOfOMRCornerMarks(Integer numberOfOMRCornerMarks) {
		this.numberOfOMRCornerMarks = numberOfOMRCornerMarks;
	}

	/**
	 * Testa se todas as coordenadas de marcas OMR necessárias foram encontradas
	 * 
	 * @return
	 */
	@Transient
	public boolean isAllCornerOMRMarksfound() {
		return this.examOMRMetadataVO.size() >= this.numberOfOMRCornerMarks;
	}

	/**
	 * @param examVO
	 * @return
	 */
	public java.awt.geom.Point2D.Double determineBottomRightCorner() {
		
		if(bottomRightCorner==null){
				
			Map<String, ExamOMRMetadataVO> mapExamOMRMetadata = getExamOMRMetadataVO();
	
			Collection<ExamOMRMetadataVO> examOMRMetadataVOs = mapExamOMRMetadata.values();
	
			BeanComparator xComparator = new BeanComparator("x");
			BeanComparator yComparator = new BeanComparator("y");
	
			ExamOMRMetadataVO metaDataX = Collections.max(examOMRMetadataVOs, xComparator);
			ExamOMRMetadataVO metaDataY = Collections.max(examOMRMetadataVOs, yComparator);
	
			double x = metaDataX.getX();
			double y = metaDataY.getY();
		
			bottomRightCorner = new java.awt.geom.Point2D.Double(x, y);

		}
		
		return bottomRightCorner;
	}

	
	public java.awt.geom.Point2D.Double determineTopLeftCorner() {

		if(topLeftCorner==null){
			
			Map<String, ExamOMRMetadataVO> mapExamOmrMetadata = getExamOMRMetadataVO();
	
			Collection<ExamOMRMetadataVO> omrMetadatas = mapExamOmrMetadata.values();
	
			BeanComparator xComparator = new BeanComparator("x");
			BeanComparator yComparator = new BeanComparator("y");
	
			ExamOMRMetadataVO x = Collections.min(omrMetadatas, xComparator);
			ExamOMRMetadataVO y = Collections.min(omrMetadatas, yComparator);
	
			this.topLeftCorner = new java.awt.geom.Point2D.Double(x.getX(), y.getY());
		}

		return this.topLeftCorner;
	}

	
	@Transient
	public Double getExamRefWidth() {
		if (examRefWidth == null) {
			Point2D.Double brCornerExam = determineBottomRightCorner();
			Point2D.Double tlCornerExam = determineTopLeftCorner();
			examRefWidth = brCornerExam.getX() - tlCornerExam.getX();
		}
		
		return examRefWidth;
	}

	@Transient
	public Double getExamRefHeigth() {
		if(examRefHeigth==null){
			Point2D.Double brCornerExam = determineBottomRightCorner();
			Point2D.Double tlCornerExam = determineTopLeftCorner();
			examRefHeigth = brCornerExam.getY() - tlCornerExam.getY();
		}
		
		return examRefHeigth;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
}
