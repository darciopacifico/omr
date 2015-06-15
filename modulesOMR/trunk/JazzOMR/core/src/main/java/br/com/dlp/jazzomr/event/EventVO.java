/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.base.AbstractBelongsOrgVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.results.EProcessingState;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;

/**
 * @author darcio
 *
 */
@FetchProfile(name="event_participations",fetchOverrides={
		@FetchProfile.FetchOverride(mode=FetchMode.JOIN, association = "participations", entity = EventVO.class)
})	
@Entity
@JazzClass(name="Evento")
public class EventVO extends AbstractBelongsOrgVO<Long>  {

	private static final long serialVersionUID = -4019759324155704476L;

	private String description;
	
	private Date dtInicio;
	private Date dtFim;
	private List<ParticipationVO> participations = new ArrayList<ParticipationVO>(80);  

	
	private Double altTotais;
	private Double altResult;
	private Double disTotais;
	private Double disResult;
	private Double disResDisp;
	
	public EventVO() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		
		
		
		
		return this.pk;
	}
	

	
	@Transient
	public List<CriterionResultVO> getCriterionResults(){
		
		List<CriterionResultVO> results = new ArrayList<CriterionResultVO>(100);
		
		List<ParticipationVO> parts = getParticipations();
		
		for (ParticipationVO participationVO : parts) {
			
			List<CriterionResultVO> criterionResults = participationVO.getCriterionResults();
			for (CriterionResultVO criterionResultVO : criterionResults) {
				
				if(criterionResultVO!=null){
					results.add(criterionResultVO);
				}
			}
			
		}
		
		return results;
	}

	
	
	@Transient
	public List<CriterionCoordinateVO> getCriterionCoords(){
		
		List<CriterionCoordinateVO> results = new ArrayList<CriterionCoordinateVO>(100);
		
		List<ParticipationVO> parts = getParticipations();
		
		for (ParticipationVO participationVO : parts) {
			List<CriterionCoordinateVO> coords = participationVO.getExamVariantVO().getCriterionCoordinates();
			
			for (CriterionCoordinateVO criterionCoordinateVO : coords) {
				
				if(criterionCoordinateVO!=null){
					results.add(criterionCoordinateVO);
				}
				
			}
			
		}
		
		return results;
	}

	
	/**
	 * @return the participations
	 */
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="fk_event")
	@JazzProp(name = "Participações",searchable=false,listable=false)
	//@IndexColumn(name="PK") retirei o index column para parar de criar itens nulos 
	public List<ParticipationVO> getParticipations() {
		return participations;
	}
	
	/**
	 * @return the description
	 */
	@NotEmpty(message="Por favor, preencha a descrição do evento.")
	@Column(length=200)
	@JazzProp(name="Descrição",comparison=ComparisonOperator.LIKE,sortable=true,tip="Descrição do Evento")
	public String getDescription() {
		return description;
	}

	/**
	 * @return the dtFim
	 */
	@NotNull(message="Por favor, preencha a data final do evento.")
	@JazzProp(name="Fim",comparison=ComparisonOperator.RANGE,sortable=true,tip="Data/Hora final do evento",listable=true)
	public Date getDtFim() {
		return dtFim;
	}

	/**
	 * @return the dtInicio
	 */
	@JazzProp(name="Início",comparison=ComparisonOperator.RANGE,sortable=true,tip="Data/Hora de início do evento",listable=true)
	@NotNull(message="Por favor, preencha a data inicial do evento.")
	public Date getDtInicio() {
		return dtInicio;
	}
	
	
	@Transient
	public Map<EProcessingState, Integer> getCountMap(){
		
		Map<EProcessingState, Integer> totalCountMap = ParticipationVO.createEmptyCountMap();
		
		Collection<ParticipationVO> participationVOs = getParticipations();
		
		for (ParticipationVO participationVO : participationVOs) {
		
			Set<EProcessingState> keys = totalCountMap.keySet();
			Map<EProcessingState, Integer> countMap = participationVO.getCountMap();
			
			for (EProcessingState eProcessingState : keys) {
				Integer count = countMap.get(eProcessingState);
				
				Integer total = totalCountMap.get(eProcessingState);
				
				totalCountMap.put(eProcessingState, total+count);
				
			}
			
		}
		
		return totalCountMap;
		
	}
	
	
	@Transient
	public Map<EProcessingState, Integer> getPercentCountMap() {
		Map<EProcessingState,Integer>  percentCountMap = new HashMap<EProcessingState, Integer>(0);
		/*
		Map<EProcessingState, Integer> countMap = getCountMap();
		Map<EProcessingState, Integer> percentCountMap = ParticipationVO.toCountMap(countMap);
		*/
		return percentCountMap;
	}
	

	@Transient
	public Set<EProcessingState> getPercentCountKeys() {
		Map<EProcessingState, Integer> percentCountMap = getPercentCountMap();
		Set<EProcessingState> keySet = percentCountMap.keySet();
		return keySet;
	}
	
	
	@Transient
	public Integer getPercentCountValue(EProcessingState state) {
		Map<EProcessingState, Integer> percentCountMap = getPercentCountMap();
		Integer floatValue = percentCountMap.get(state);
		return floatValue;
	}
	
	
	@Transient
	public Date getStartProc(){
		Date dt = (Date) getMin("dtInc", getParticipations());
		return dt;
	}
	
	@Transient
	public Date getEndProc(){
		
		boolean processTerminated = isProcessTerminated();
		
		if(processTerminated){
			Collection<ParticipationVO> participationVOs = getParticipations();
			return (Date) getMax("dtAlt", participationVOs);
		}
		
		return null;
	}

	@Transient
	public boolean isProcessTerminated() {
		Collection<ParticipationVO> participationVOs = getParticipations();
		
		boolean processTerminated = true;
		for (ParticipationVO participationVO : participationVOs) {
			
			if(!participationVO.isProcessTerminated()){
				processTerminated=false;
				break;
			}
			
		}
		return processTerminated;
	}
	
	@Transient
	public Long getTimeSpent(){
		
		Collection<ParticipationVO> participa = getParticipations();
		
		long totalTimeSpent=0;
		
		for (ParticipationVO participationVO : participa) {
			
			long timeSpent = participationVO.getTimeSpent();
			
			totalTimeSpent = totalTimeSpent + timeSpent;
			
		}
		
		return totalTimeSpent;
	}
			
	
	
	/**
	 * @param participations the participations to set
	 */
	public void setParticipations(List<ParticipationVO> participations) {
		this.participations = participations;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * @param dtFim the dtFim to set
	 */
	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}


	/**
	 * @param dtInicio the dtInicio to set
	 */
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}



	@Transient
	public Double getAltTotais() {
		return altTotais;
	}


	@Transient
	public Double getAltResult() {
		return altResult;
	}


	@Transient
	public Double getDisTotais() {
		return disTotais;
	}


	@Transient
	public Double getDisResult() {
		return disResult;
	}


	@Transient
	public Double getPercDigitalizacao(){
		
		double criteriosTotais = altTotais+disTotais;
		double criteriosDigitalizados = altResult+disResult+disResDisp;
		
		double percDigitalizacao = criteriosDigitalizados/criteriosTotais;
		return percDigitalizacao;
		
	}
	
	
	public void setAltTotais(Double altTotais) {
		this.altTotais = altTotais;
	}



	public void setAltResult(Double altResult) {
		this.altResult = altResult;
	}



	public void setDisTotais(Double disTotais) {
		this.disTotais = disTotais;
	}



	public void setDisResult(Double disResult) {
		this.disResult = disResult;
	}



	public Double getDisResDisp() {
		return disResDisp;
	}



	public void setDisResDisp(Double disResDisp) {
		this.disResDisp = disResDisp;
	}

}
