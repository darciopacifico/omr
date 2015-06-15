/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.annotations.IndexColumn;

import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.person.GrupoVO;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.EProcessingState;
import br.com.dlp.jazzomr.results.PayloadVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * @author darcio
 * 
 */
@Entity
@JazzClass(name = "Participação", voMestre = EventVO.class, baseEntity = false)
@FetchProfiles({
		
	@FetchProfile(name = "participations_examvariant", fetchOverrides = {
				@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "examVariantVO", entity = ParticipationVO.class),
				@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "pessoaVO", entity = ParticipationVO.class),
				@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "grupoVO", entity = ParticipationVO.class) }),
		
	@FetchProfile(name = "participations_payloadVOs", fetchOverrides = {
			@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "examVariantVO", entity = ParticipationVO.class),
			@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "pessoaVO", entity = ParticipationVO.class),
			@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "grupoVO", entity = ParticipationVO.class) }),

	@FetchProfile(name = "participacao_com_payloadsImagens", fetchOverrides = {
				@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "payloadVOs", entity = ParticipationVO.class),
				@FetchProfile.FetchOverride(mode = FetchMode.JOIN, association = "imageVOs", entity = PayloadVO.class) }) 
						
})
				
				
public class ParticipationVO extends AbstractLogEntityVO {

	private static final long serialVersionUID = 2512255172110878569L;

	private PessoaVO pessoaVO;
	private GrupoVO grupoVO;

	private ExamVariantVO examVariantVO;

	private List<CriterionResultVO> criterionResults = new ArrayList<CriterionResultVO>();

	private List<PayloadVO> payloadVOs = new ArrayList<PayloadVO>();

	
	@Transient
	public Double getNota(){
		
		
		double countMaxNota = 0;
		double countNota= 0;
		
		List<QuestionnaireVO> qtns = examVariantVO.getExamVO().getQuestionnaires();
		
		
		for (QuestionnaireVO questionnaireVO : qtns) {
			List<QuestionVO> questions = questionnaireVO.getQuestions();
			
			for (QuestionVO questionVO : questions) {
				countMaxNota++;
				countNota = countNota + questionVO.getNota(this);
			}
		}
		
		
		if(countMaxNota>0){
			
			return (countNota/countMaxNota)*10;
			
		}else{
			return 0d;
		}
		
		
	}
	

	
	@Transient
	public Double getPercCorrection(){
		
		
		double countMaxPerc = 0;
		double countPerc= 0;
		
		List<QuestionnaireVO> qtns = examVariantVO.getExamVO().getQuestionnaires();
		
		
		for (QuestionnaireVO questionnaireVO : qtns) {
			List<QuestionVO> questions = questionnaireVO.getQuestions();
			
			for (QuestionVO questionVO : questions) {
				
				countMaxPerc = countMaxPerc + 1;
				countPerc = countPerc + questionVO.getPercCorrecao(this);
				
			}
		}
		
		
		if(countMaxPerc>0){
			
			return (countPerc/countMaxPerc);
			
		}else{
			return 0d;
		}
		
		
	}
	
	
	public ParticipationVO() {
	}

	@Transient
	public Map<AbstractCriterionVO,CriterionResultVO> resultMap = null;
	

	@Transient
	public Map<AbstractCriterionVO, CriterionResultVO> getResultMap() {
		
		if(resultMap==null){
			resultMap = new HashMap<AbstractCriterionVO, CriterionResultVO>(criterionResults.size());
			for (CriterionResultVO criterionResultVO : criterionResults) {
				resultMap.put(criterionResultVO.getCriterionCoordinateVO().getAbstractCriterionVO(), criterionResultVO);
			}
		}
		
		return resultMap;
	}


	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "participationVO")
	@Fetch(FetchMode.SELECT)
	public List<PayloadVO> getPayloadVOs() {
		return payloadVOs;
	}

	/**
	 * @return the examVariantVO
	 */
	@ManyToOne
	@Fetch(FetchMode.JOIN)
	public ExamVariantVO getExamVariantVO() {
		return examVariantVO;
	}

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}

	/**
	 * @return the questionResults
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participationVO")
	@IndexColumn(name = "PK")
	@OrderColumn(name = "PK")
	@Fetch(FetchMode.SELECT)
	public List<CriterionResultVO> getCriterionResults() {
		return criterionResults;
	}

	
	
	@Transient
	public Date getStartProc() {
		Collection<PayloadVO> payloadVOs = getPayloadVOs();

		Date dtInc = (Date) getMin("dtInc", payloadVOs);

		return dtInc;
	}

	/**
	 * Recupera data de finalizacao do processamento desta participacao
	 * 
	 * @return
	 */
	@Transient
	public Date getEndProc() {

		Date dtFim = null;

		if (isProcessTerminated()) {
			Collection<PayloadVO> payloadVOs = getPayloadVOs();
			dtFim = (Date) getMax("dtAlt", payloadVOs);
		}

		return dtFim;
	}

	/**
	 * Testa se todas as paginas foram enviadas por upload e todas foram processadas
	 * 
	 * @return
	 */
	@Transient
	public boolean isProcessTerminated() {
		boolean isAllTerminated = true;
		if (!isAllPagesUploaded()) {
			return false;
		}

		for (PayloadVO payloadVO : payloadVOs) {
			if (!payloadVO.getProcessingState().equals(EProcessingState.TERMINATED)) {
				isAllTerminated = false;
				break;
			}
		}

		return isAllTerminated;
	}

	/**
	 * Testa se todas as paginas referentes a esta participacao foram enviadas (via upload)
	 * 
	 * @return
	 */
	@Transient
	protected boolean isAllPagesUploaded() {

		Integer totalPages = getExamVariantVO().getExamVO().getTotalPages();

		Integer sendedPages = getCountSendedPages();

		return sendedPages >= totalPages;
	}

	/**
	 * Conta unicamente as paginas enviadas
	 * 
	 * @return
	 */
	@Transient
	public Integer getCountSendedPages() {
		Map<Long, PayloadVO> mapPayload = getUniqueByPage();

		Integer sendedPages = mapPayload.keySet().size();
		return sendedPages;
	}

	/**
	 * Conta quantas paginas ja foram enviadas unicamente
	 * 
	 * @return
	 */
	@Transient
	public Map<Long, PayloadVO> getUniqueByPage() {
		Collection<PayloadVO> payloadVOs = getPayloadVOs();
		Map<Long, PayloadVO> mapPayload = new HashMap<Long, PayloadVO>(payloadVOs.size());

		for (PayloadVO payloadVO : payloadVOs) {
			mapPayload.put(payloadVO.getPK(), payloadVO);
		}
		return mapPayload;
	}

	/**
	 * Conta o tempo total para processamento gasto ate o momento ou ate o processamento completo
	 * 
	 * @return
	 */
	@Transient
	public Long getTimeSpent() {

		Collection<PayloadVO> payloads = getPayloadVOs();

		long totalTimeSpent = 0;

		for (PayloadVO payloadVO : payloads) {
			totalTimeSpent = +payloadVO.getTimeSpent();
		}

		return totalTimeSpent;
	}

	/**
	 * Conta o percentual de conclusao desta participacao
	 * 
	 * @deprecated
	 * @return
	 */
	@Transient
	public Float getFulfilled() {
		Map<EProcessingState, Integer> countMap = getCountMap();

		float terminated = countMap.get(EProcessingState.TERMINATED);

		float totalPages = getExamVariantVO().getExamVO().getTotalPages();

		return (terminated / totalPages);
	}

	/**
	 * Retorna um mapa que conta a quantidade de imagens (payload) por estado de processamento
	 * 
	 * @return
	 */
	@Transient
	public Map<EProcessingState, Integer> getCountMap() {

		Map<EProcessingState, Integer> countMap = createEmptyCountMap();
		countMap.put(EProcessingState.NOT_CREATED, getCountNotSendedPayloads());

		Map<Long, PayloadVO> payLoads = getUniqueByPage();
		Collection<PayloadVO> values = payLoads.values();

		for (PayloadVO payloadVO : values) {

			EProcessingState state = payloadVO.getProcessingState();

			Integer actualCount = countMap.get(state);
			actualCount = actualCount == null ? 0 : actualCount;

			countMap.put(state, actualCount + 1);
		}

		return countMap;
	}

	@Transient
	public int getCountNotSendedPayloads() {
		Integer totalPages = getExamVariantVO().getExamVO().getTotalPages();
		Integer sendedPages = getCountSendedPages();
		int notSendedPayloads = totalPages - sendedPages;

		notSendedPayloads = notSendedPayloads < 0 ? 0 : notSendedPayloads;

		return notSendedPayloads;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Set<Integer> getNotUploadedPages() {
		
		Integer totalPages = getExamVariantVO().getExamVO().getTotalPages();
		Set<Integer> pageSet = new HashSet<Integer>(totalPages);
		
		for (int i = 1; i <= totalPages; i++) {
			pageSet.add(i);
		}
				
		for (PayloadVO payloadVO : getPayloadVOs()) {
			if (payloadVO.getProcessingState().equals(EProcessingState.TERMINATED)) {
				pageSet.remove(payloadVO.getPage());
			}
		}
		
		return pageSet;
	}

	@Transient
	public Map<EProcessingState, Integer> getPercentCountMap() {

		Map<EProcessingState, Integer> countMap = getCountMap();

		Map<EProcessingState, Integer> percMap = toCountMap(countMap);

		return percMap;
	}

	/**
	 * Conta o percentual de cada estado de processo
	 * 
	 * @return
	 */
	@Transient
	public static Map<EProcessingState, Integer> toCountMap(Map<EProcessingState, Integer> countMap) {

		Map<EProcessingState, Integer> retCountMap = new TreeMap<EProcessingState, Integer>();

		Collection<Integer> counts = countMap.values();
		float totalCount = 0;
		for (Integer count : counts) {
			totalCount = totalCount + count;
		}

		if (totalCount > 0) {
			retCountMap.putAll(countMap);

		} else {
			retCountMap.put(EProcessingState.NO_PARTICIPATIONS, 1);
		}

		return retCountMap;
	}

	/**
	 * Cria um mapa de contagem para os estados de processo. Retorna um mapa carregado com cada estado de processo possivel e contador zerado
	 * 
	 * @return
	 */
	@Transient
	public static Map<EProcessingState, Integer> createEmptyCountMap() {
		Map<EProcessingState, Integer> countMap = new HashMap<EProcessingState, Integer>();

		EnumSet<EProcessingState> processStates = EnumSet.range(EProcessingState.NOT_CREATED, EProcessingState.FAILED);

		for (EProcessingState eProcessingState : processStates) {
			countMap.put(eProcessingState, 0);
		}

		return countMap;
	}

	@Transient
	public Float getErrors() {
		return null;
	}


	/**
	 * @param pessoaVO
	 *          the pessoaVO to set
	 */
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	/**
	 * @param examVariantVO
	 *          the examVariantVO to set
	 */
	public void setExamVariantVO(ExamVariantVO examVariantVO) {
		this.examVariantVO = examVariantVO;
	}

	/**
	 * @return the grupoVO
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	public GrupoVO getGrupoVO() {
		return grupoVO;
	}

	/**
	 * @param grupoVO
	 *          the grupoVO to set
	 */
	public void setGrupoVO(GrupoVO grupoVO) {
		this.grupoVO = grupoVO;
	}

	public void setPayloadVOs(List<PayloadVO> payloadVOs) {
		this.payloadVOs = payloadVOs;
	}

	public void setCriterionResults(List<CriterionResultVO> criterionResults) {
		this.resultMap=null;
		this.criterionResults = criterionResults;
	}

}
