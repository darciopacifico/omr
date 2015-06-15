package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;
import br.com.jazz.codegen.enums.JazzRenderer;
import br.com.jazz.jrds.annotations.Navigable;

/**
 * @author darcio
 *
 */
public class QuestionVO extends AbstractBaseVO<Long> implements ISortable {
	
	private static final long serialVersionUID = 6331211289004549450L;
	
	private String description;
	private String resumo;

	private Double ordem;
	private Set<CriterionVO> criterionVOs = new HashSet<CriterionVO>(20);
	private List<ImageVO> imageVOs = new ArrayList<ImageVO>(2);
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@XmlTransient
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
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
	@JazzProp(name = "Descrição", searchable = true, comparison = ComparisonOperator.LIKE, renderer = JazzRenderer.WYSIWYG)
	@Lob
	@NotEmpty(message="A descrição da questão não pode ficar em branco!")
	public String getDescription() {
		return description;
	}


	/**
	 * @return the criterionVOs
	@Sort(type=SortType.COMPARATOR,comparator=CriterionComparator.class)
	 */
	@JazzProp(name="Critérios",renderer=JazzRenderer.GRID)
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)//atribui o orphan removal aa colecao de criterios. 20h 170611. os criterios tem de ser apagados
	@JoinColumn(name="fk_question")
	@Fetch(FetchMode.SELECT)
	@Valid
	@Navigable(alias="crt", visitorClass=ShuffleCriterionsVisitor.class)
	
	public Set<CriterionVO> getCriterionVOs() {
		
		return criterionVOs;
	}
	
		


	

	@XmlTransient
	@Transient
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


	/**
	 * @param selectedAlternatives
	 * @return
	 */
	protected List<? extends CriterionVO > fixCriterionOrder(Collection<? extends CriterionVO> selectedAlternatives) {
		List<CriterionVO> alternativeVOList = new ArrayList<CriterionVO>(selectedAlternatives);
		
		Collections.sort(alternativeVOList, ISortable.sortableComparator);
		
		for (CriterionVO alternativeVO : alternativeVOList) {
			
			if(alternativeVO.getOrdem()==null){
				alternativeVO.setOrdem(new Double(alternativeVOList.indexOf(alternativeVO)));
			}
			
		}
		return alternativeVOList;
	}


	@Column(length=200)
	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}


	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.question.ISortable#getOrder()
	 */
	@Override
	@Column
	public Double getOrdem() {
		return ordem;
	}



	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.question.ISortable#setOrder(java.lang.Integer)
	 */
	@Override
	public void setOrdem(Double order) {
		this.ordem = order;
	}
	
}
