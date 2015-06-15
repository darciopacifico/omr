/**
 * 
 */
package br.com.dlp.jazzomr.empresa;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanComparator;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.dlp.jazzomr.exam.ExamOMRMetadataVO;
import br.com.dlp.jazzomr.results.JRFileVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Relatorio para exames. Contém relatorio jasper
 * @author darcio
 */
@Entity
@JazzClass(name = "Relatórios")
/*
 */
@FetchProfiles(value={
		
		@FetchProfile(name="relatorio_com_metadados", fetchOverrides={
				@FetchProfile.FetchOverride(entity=RelatorioVO.class, association="examOMRMetadataVO",	mode=FetchMode.JOIN)
		}),
		@FetchProfile(name="relatorio_com_jasper", fetchOverrides={
				@FetchProfile.FetchOverride(entity=RelatorioVO.class, association="jrFileVOs",	mode=FetchMode.JOIN)
		}),
		
})  
public class RelatorioVO extends AbstractLogEntityVO<Long> {
	private static final long serialVersionUID = 6299205749761077483L;
	
	private ETipoRelatorio tipoRelatorio;
	private String description;
	
	private List<JRFileVO> jrFileVOs = new ArrayList<JRFileVO>(2);

	private Integer numberOfOMRCornerMarks=2;
	private Map<String, ExamOMRMetadataVO> examOMRMetadataVO = new HashMap<String, ExamOMRMetadataVO>(3);
	
	private Integer totalPages=1;
	
	protected java.awt.geom.Point2D.Double topLeftCorner;
	protected java.awt.geom.Point2D.Double bottomRightCorner;
	protected Double examRefWidth;
	protected Double examRefHeigth; 
	
	public RelatorioVO() {
	}

	public RelatorioVO(Long pk) {
		super(pk);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return pk;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	


	/**
	 * @param numberOfOMRCornerMarks
	 *          the numberOfOMRCornerMarks to set
	 */
	public void setNumberOfOMRCornerMarks(Integer numberOfOMRCornerMarks) {
		this.numberOfOMRCornerMarks = numberOfOMRCornerMarks;
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
	
	@NotBlank(message="A descrição do relatório não pode ficar em branco")
	@Length(min=1,max=150)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
	/**
	 * @return the examOMRMetadataVO
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_relatorio")
	@MapKey(name = "omrKey")
	@JazzProp(name="",ignore=true)
	@Fetch(FetchMode.SELECT)
	public Map<String, ExamOMRMetadataVO> getExamOMRMetadataVO() {
		return examOMRMetadataVO;
	}

	public void setExamOMRMetadataVO(Map<String, ExamOMRMetadataVO> examOMRMetadataVO) {
		this.examOMRMetadataVO = examOMRMetadataVO;
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

	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
	@JoinColumn(name="fk_relatorio")
	public List<JRFileVO> getJrFileVOs() {
		return jrFileVOs;
	}

	public void setJrFileVOs(List<JRFileVO> jrFileVOs) {
		this.jrFileVOs = jrFileVOs;
	}

	
	@Override
	public String toString() {
		return this.pk + ". "+ this.description;
	}

	@Enumerated(EnumType.STRING)
	public ETipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(ETipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

}
