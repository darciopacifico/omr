/**
 * 
 */
package br.com.dlp.jazzomr.empresa;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.BeanComparator;

import br.com.dlp.jazzomr.AbstractBaseVO;
import br.com.dlp.jazzomr.exam.ExamOMRMetadataVO;

/**
 * Relatorio para exames. Contém relatorio jasper
 * @author darcio
 */
public class RelatorioVO extends AbstractBaseVO<Long> {
	private static final long serialVersionUID = 6299205749761077483L;
	
	private Integer numberOfOMRCornerMarks=2;
	private Map<String, ExamOMRMetadataVO> examOMRMetadataVO = new HashMap<String, ExamOMRMetadataVO>(3);
	
	protected java.awt.geom.Point2D.Double topLeftCorner;
	protected java.awt.geom.Point2D.Double bottomRightCorner;
	protected Double examRefWidth;
	protected Double examRefHeigth; 
	
	
	public RelatorioVO() {
	}

	public RelatorioVO(Long pk) {
		super(pk);
	}

	@Override
	public Long getPK() {
		return pk;
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

	
	@XmlTransient
	public Double getExamRefWidth() {
		if (examRefWidth == null) {
			Point2D.Double brCornerExam = determineBottomRightCorner();
			Point2D.Double tlCornerExam = determineTopLeftCorner();
			examRefWidth = brCornerExam.getX() - tlCornerExam.getX();
		}
		
		return examRefWidth;
	}

	@XmlTransient
	public Double getExamRefHeigth() {
		if(examRefHeigth==null){
			Point2D.Double brCornerExam = determineBottomRightCorner();
			Point2D.Double tlCornerExam = determineTopLeftCorner();
			examRefHeigth = brCornerExam.getY() - tlCornerExam.getY();
		}
		
		return examRefHeigth;
	}
	
	/**
	 * @return the examOMRMetadataVO
	 */
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
	
	@XmlTransient
	public boolean isAllCornerOMRMarksfound() {
		return this.examOMRMetadataVO.size() >= this.numberOfOMRCornerMarks;
	}

	@Override
	public String toString() {
		return this.pk+"";
	}

}
