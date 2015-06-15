package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import br.com.dlp.jazzomr.AbstractBaseVO;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.jrds.annotations.Navigable;

/**
 * @author darcio
 *
 */
public class QuestionVO extends AbstractBaseVO<Long>{
	
	private static final long serialVersionUID = 6331211289004549450L;
	
	private String description;
	private String resumo;

	private Double ordem;
	private Set<CriterionVO> criterionVOs = new HashSet<CriterionVO>(20);
	private List<ImageVO> imageVOs = new ArrayList<ImageVO>();
	
	
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	@Override
	public void setPK(Long ppk) {
		// TODO Auto-generated method stub
		super.setPK(ppk);
	}
	
	
	/**
	 * @return the imageVOs
	 */
	
	public List<ImageVO> getImageVOs() {
		
		return imageVOs;
	}	

	
	
	/**
	 * @param alternatives
	 * @param correctAlternativeScore
	 * @return
	 */
	public Integer countCorrectScore() {
		return 1;
	}
	
	
	public void setImageVOs(List<ImageVO> imageVOs) {
		this.imageVOs = imageVOs;
	}

	public QuestionVO() {
	}
	
	public static class CriterionComparator implements Comparator<CriterionVO>{
		
		public CriterionComparator(){}
		
		@Override
		public int compare(CriterionVO crit1, CriterionVO crit2) {
			
			if(crit1 ==null || crit2==null){
				return 0;
			}
			
			
			if(crit1.getIsAlternative() && crit2.getIsAlternative() ){
				
				
				if(crit1.getPK()!=null && crit2.getPK()!=null){
					return (int) (crit1.getPK()-crit2.getPK());
					
				}else{
					
					return -1;
				}
				
				
			}else if(crit1.getIsAlternative() && crit2.getIsDissertative() ){
				return -1;
				
			}else if(crit1.getIsDissertative() && crit2.getIsAlternative() ){
				return 1;
			}			
			
			return 0;
		}
		
	}

	
	/**
	 * @param descWiki
	 */
	public QuestionVO(String descWiki) {
		super();
		this.description = descWiki;
	}
	
	
	/**
	 * @return the descWiki
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @return the criterionVOs
	@Sort(type=SortType.COMPARATOR,comparator=CriterionComparator.class)
	 */
	@Navigable(alias="crt", visitorClass=ShuffleCriterionsVisitor.class)
	
	public Set<CriterionVO> getCriterionVOs() {
		
		return criterionVOs;
	}
	
		


	

	@XmlTransient
	public List<CriterionVO> getCriterionVOList() {
		
		if(criterionVOs==null){
			return null;
		}
		
		ArrayList<CriterionVO> criterionsList = new ArrayList<CriterionVO>(criterionVOs);
		Collections.sort(criterionsList,new CriterionComparator());
		
		return criterionsList;
	}
	
	/**
	 * @param description the descWiki to set
	 */
	public void setDescription(String descriptionWiki) {
		this.description = descriptionWiki;
	}


	/**
	 * @param criterionVOs the criterionVOs to set
	 */
	public void setCriterionVOs(Set<CriterionVO> criterionVOs) {
		this.criterionVOs = criterionVOs;
	}


	


	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}


	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.question.ISortable#getOrder()
	 */
	public Double getOrdem() {
		return ordem;
	}



	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.question.ISortable#setOrder(java.lang.Integer)
	 */
	public void setOrdem(Double order) {
		this.ordem = order;
	}
	
}
