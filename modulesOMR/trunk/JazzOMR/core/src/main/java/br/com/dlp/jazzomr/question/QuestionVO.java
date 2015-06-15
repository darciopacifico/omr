package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.base.AbstractBelongsOrgVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;
import br.com.jazz.codegen.enums.JazzRenderer;

/**
 * @author darcio
 *
 */
@FetchProfiles(value={
		@FetchProfile(name="questao_com_alternativas", fetchOverrides={
				@FetchProfile.FetchOverride(entity=QuestionVO.class	, association="criterionVOs", mode=FetchMode.JOIN)
		}),
		@FetchProfile(name="questao_com_imagens", fetchOverrides={
				@FetchProfile.FetchOverride(entity=QuestionVO.class	, association="imageVOs", mode=FetchMode.JOIN),
				@FetchProfile.FetchOverride(entity=QuestionVO.class	, association="criterionVOs", mode=FetchMode.JOIN)
		}),
})  
/*
 * */
@Entity
@CheckQuestionTypeConsistency
@JazzClass(name="Questão",voMestre=QuestionnaireVO.class)
public class QuestionVO extends AbstractBelongsOrgVO<Long> implements ISortable {
	
	private static final long serialVersionUID = 6331211289004549450L;
	
	private String description;
	private String resumo;

	private Double order;
	private Set<AbstractCriterionVO> criterionVOs = new HashSet<AbstractCriterionVO>(20);
	private List<ImageVO> imageVOs = new ArrayList<ImageVO>(2);
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	
	/**
	 * @return the imageVOs
	 */
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
	public List<ImageVO> getImageVOs() {
		return imageVOs;
	}	

	
	
	/**
	 * @param alternatives
	 * @param correctAlternativeScore
	 * @return
	 */
	public Integer countCorrectScore() {
		Collection<AlternativeVO> alternatives = getAlternativeVOs();
		
		Integer correctAlternativeScore=0;
		for (AlternativeVO alternativeVO : alternatives) {
			if(alternativeVO!=null && alternativeVO.getCorrect()){
				correctAlternativeScore++;
			}
		}
		
		return correctAlternativeScore;
	}
	
	
	public void setImageVOs(List<ImageVO> imageVOs) {
		this.imageVOs = imageVOs;
	}

	public QuestionVO() {
	}
	
	public static class CriterionComparator implements Comparator<AbstractCriterionVO>{
		
		public CriterionComparator(){}
		
		@Override
		public int compare(AbstractCriterionVO crit1, AbstractCriterionVO crit2) {
			
			if(crit1 ==null || crit2==null){
				return 0;
			}
			
			
			if(crit1.getIsAlternative() && crit2.getIsAlternative() ){
				
				AlternativeVO alt1 = (AlternativeVO) crit1;
				AlternativeVO alt2 = (AlternativeVO) crit2;
				
				if(alt1.getPK()!=null && alt2.getPK()!=null){
					return (int) (alt1.getPK()-alt2.getPK());
					
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
	public Set<AbstractCriterionVO> getCriterionVOs() {
		
		return criterionVOs;
	}
	
	
	/**
	 * Calcula nota para esta questao 
	 * @param participationVO
	 * @return
	 */
	@Transient
	public Double getNota(ParticipationVO participationVO){
		
		if(participationVO==null){
			return 0d;
		}
		
		Map<AbstractCriterionVO, CriterionResultVO> resultMap = participationVO.getResultMap();
		
		if(resultMap==null){
			return 0d;
		}
		
		Set<AbstractCriterionVO> criterions = getCriterionVOs();

		Double acertosAlt = 0d;
		Double pontosDiss = 0d;
		Double pontosMaxDiss = 0d;

		boolean hasAlternative = false;
		boolean hasDissertative = false;
		
		
		//soma os pontos das partes alternativas e dissertativas da questao
		for (AbstractCriterionVO abstractCriterionVO : criterions) {
			
			CriterionResultVO criterionResultVO = resultMap.get(abstractCriterionVO);
			if(criterionResultVO!=null){
				
				if(abstractCriterionVO.getIsAlternative()){
					
					hasAlternative = true;
					AlternativeVO alternativeVO = (AlternativeVO) abstractCriterionVO;
					
					if(isAcertouAlternativa(criterionResultVO, alternativeVO)){
						acertosAlt = acertosAlt +1;
					}
					
				}else{
					
					hasDissertative=true;
					DissertativeVO dissertativeVO = (DissertativeVO) abstractCriterionVO;
					
					pontosDiss = pontosDiss + (criterionResultVO.getPontuacao()==null?0:criterionResultVO.getPontuacao());
					pontosMaxDiss = pontosMaxDiss + (dissertativeVO.getMaxPonto()==null?0:dissertativeVO.getMaxPonto());
					
				}
			}
			
		}
		
		//soma os pontos encontrados nas partes alternativas e dissertativas
		Double notaAlternativa=0d;
		Double notaDissertativa=0d;
		Double notaGeral = 0d;
		
		if(hasAlternative){
			Double correctAlters = new Double(getCorrectAlternatives());
			notaAlternativa = acertosAlt/correctAlters;
		}
		
		if(hasDissertative){
			notaDissertativa = pontosDiss/pontosMaxDiss;
		}
		
		if(hasAlternative && hasDissertative){
			//media simples das duas partes da questao // TODO: PONDERAR MEDIA
			notaGeral = (notaAlternativa+notaDissertativa)/2; 
			
		}else if (hasAlternative){
			notaGeral = notaAlternativa;
			
		}else if (hasDissertative){
			notaGeral = notaDissertativa;
			
		}
		
		return notaGeral;
	}
	
	
	/**
	 * Conta a quantidade esperada de alternativas corretas para esta questão
	 * @return
	 */
	@Transient
	private Integer getCorrectAlternatives() {
		Collection<AlternativeVO> alternatives = getAlternativeVOs();
		
		Integer correctAlternatives = 0;
		if(CollectionUtils.isNotEmpty(alternatives)){
			for (AlternativeVO alternativeVO : alternatives) {
				if(alternativeVO.getCorrect()){
					correctAlternatives++;
				}
			}
		}
		
		return correctAlternatives;
	}

	/**
	 * Calcula percentual de correção da questão. Retorna 100% se todas as alternativas e dissertativas referentes a esta questão foram digitalizadas e processadas pelo sistema   
	 * @param participationVO
	 * @return Retorna 100% se todas as alternativas e dissertativas referentes a esta questão foram digitalizadas e processadas pelo sistema
	 */
	@Transient
	public Double getPercCorrecao(ParticipationVO participationVO){
		
		if(participationVO==null){
			return 0d;
		}
		
		Map<AbstractCriterionVO, CriterionResultVO> resultMap = participationVO.getResultMap();
		
		if(resultMap==null){
			return 0d;
		}
		
		Set<AbstractCriterionVO> criterions = getCriterionVOs();
		
		double corrigidos = 0;
		
		//soma os pontos das partes alternativas e dissertativas da questao
		for (AbstractCriterionVO abstractCriterionVO : criterions) {
			CriterionResultVO criterionResultVO = resultMap.get(abstractCriterionVO);
			
			if(criterionResultVO!=null && 
						(
								(abstractCriterionVO.getIsDissertative() && criterionResultVO.isReviewed()) 
								|| 
								(abstractCriterionVO.getIsAlternative())
						)){
				corrigidos++;
			}
		}
		
		return  (corrigidos/((double)criterions.size()));
		
	}
	
	

	/**
	 * Calcula percentual de correção da questão. Retorna 100% se todas as alternativas e dissertativas referentes a esta questão foram digitalizadas e processadas pelo sistema   
	 * @param participationVO
	 * @return Retorna 100% se todas as alternativas e dissertativas referentes a esta questão foram digitalizadas e processadas pelo sistema
	 */
	@Transient
	public Boolean getAlternativasEmBranco(ParticipationVO participationVO){
		
		Boolean isFullFilled =false ;
		Integer countChecked = 0;
		
		if(participationVO==null){
			return false;
		}
		
		Map<AbstractCriterionVO, CriterionResultVO> resultMap = participationVO.getResultMap();
		
		if(resultMap==null){
			//não se sabe ainda
			return false;
		}
		
		List<AlternativeVO> alternatives = getAlternativeVOs();
		
		
		if(CollectionUtils.isEmpty(alternatives)){
			//não possui criterios alternativos
			return false;
		}
		
		double corrigidos = 0;
		
		//soma os pontos das partes alternativas e dissertativas da questao
		for (AlternativeVO alternativeVO : alternatives) {
			CriterionResultVO criterionResultVO = resultMap.get(alternativeVO);
			
			if(criterionResultVO!=null){
				corrigidos++;
				
				if(criterionResultVO.getChecked()){
					countChecked++;
				}
				
			}
		}
		
		isFullFilled = (corrigidos/((double)alternatives.size())) == 1;

		Integer correctAlts = getCorrectAlternatives();
		
		boolean possuiAlternativasEmBranco = countChecked < correctAlts;
		
		//testa se ficaram alternativas em branco
		//testa se os criterios alternativos foram completamente processados e nenhuma alternativa foi escolhida ou foram escolhidas menos alternativas do que o esperado
		return isFullFilled && possuiAlternativasEmBranco ;
		
	}
	
	

	/**
	 * @param criterionResultVO
	 * @param alternativeVO
	 * @return
	 */
	@Transient
	protected boolean isAcertouAlternativa(CriterionResultVO criterionResultVO, AlternativeVO alternativeVO) {
		return criterionResultVO.getChecked() && AlternativeScore.CORRECT.equals(alternativeVO.getScore());
	}
	

	@Transient
	public List<AbstractCriterionVO> getCriterionVOList() {
		
		if(criterionVOs==null){
			return null;
		}
		
		ArrayList<AbstractCriterionVO> criterionsList = new ArrayList<AbstractCriterionVO>(criterionVOs);
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
	public void setCriterionVOs(Set<AbstractCriterionVO> criterionVOs) {
		this.criterionVOs = criterionVOs;
	}

	@Transient
	public List<AlternativeVO> getAlternativeVOs() {
		
		@SuppressWarnings("unchecked")
		Collection<AlternativeVO> selectedAlternatives = CollectionUtils.select(getCriterionVOs(), new Predicate() {
			public boolean evaluate(Object object) {
				return (object!=null && object instanceof AlternativeVO);
			}
		});
		
		List<AlternativeVO> alternativeVOList = (List<AlternativeVO>) fixCriterionOrder(selectedAlternatives);
		
		return alternativeVOList;
	}



	/**
	 * @param selectedAlternatives
	 * @return
	 */
	protected List<? extends AbstractCriterionVO > fixCriterionOrder(Collection<? extends AbstractCriterionVO> selectedAlternatives) {
		List<AbstractCriterionVO> alternativeVOList = new ArrayList<AbstractCriterionVO>(selectedAlternatives);
		
		Collections.sort(alternativeVOList, ISortable.sortableComparator);
		
		for (AbstractCriterionVO alternativeVO : alternativeVOList) {
			
			if(alternativeVO.getOrdem()==null){
				alternativeVO.setOrdem(new Double(alternativeVOList.indexOf(alternativeVO)));
			}
			
		}
		return alternativeVOList;
	}

	@Transient
	public List<DissertativeVO> getDissertativeVOs() {
		
		@SuppressWarnings("unchecked")
		Collection<DissertativeVO> selectedAlternatives = CollectionUtils.select(getCriterionVOs(), new Predicate() {
			public boolean evaluate(Object object) {
				return (object!=null && object instanceof DissertativeVO);
			}
		});
		
		List<DissertativeVO> alternativeVOList = (List<DissertativeVO>) fixCriterionOrder(selectedAlternatives);
		
		return alternativeVOList;
	}

	@Transient
	public void setAlternativeVOs(List<AlternativeVO> alternativeVOs) {
		this.criterionVOs.addAll(alternativeVOs);
	}

	@Transient
	public void setDissertativeVOs(List<DissertativeVO> dissertativeVOs) {
		this.criterionVOs.addAll(dissertativeVOs);
	}

	@Transient
	public Boolean getDissertative(){
		return CollectionUtils.isNotEmpty(getDissertativeVOs());
	}
	
	@Transient
	public Boolean getAlternative(){
		return CollectionUtils.isNotEmpty(getAlternativeVOs());
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
		return order;
	}



	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.question.ISortable#setOrder(java.lang.Integer)
	 */
	@Override
	public void setOrdem(Double order) {
		this.order = order;
	}
	
}
