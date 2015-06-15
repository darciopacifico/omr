/**
 * 
 */
package br.com.dlp.jazzomr.results;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.annotations.Index;

import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(voMestre=ParticipationVO.class,name="Imagem",baseEntity=false)
@FetchProfiles(value={
		
		@FetchProfile(name="payload_com_imagens", fetchOverrides={
				@FetchProfile.FetchOverride(entity=PayloadVO.class, association="imageVOs", 		mode=FetchMode.JOIN),
		}),
		
})  

public class PayloadVO extends AbstractLogEntityVO {
	
	private static final long serialVersionUID = -818208847198493027L;

	private String descricao;
	
	//oneToOne, mas sera list para nao termos problemas com fetch
	private List<ImageVO> imageVOs = new ArrayList<ImageVO>(2);
	
	private Long size;
	private String preProcessImgHash;
	private Integer page;
	
	private ParticipationVO participationVO;
	private String messageState;
	private String fullMessageState;
	
	private EProcessingState processingState=EProcessingState.CREATED;

	/** construtor padrao */
	public PayloadVO() {}

	/**
	 * @return the imageVOs
	 */
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
	public List<ImageVO> getImageVOs() {
		return imageVOs;
	}	
	
	@Transient
	public ImageVO getImageVO() {
		List<ImageVO> images = getImageVOs();
		
		if(Hibernate.isInitialized(images) && CollectionUtils.isNotEmpty(images)){
			return images.get(0);	
		}else{
			return null;
		}
	}	
	
	@ManyToOne
	@JoinColumn(name="fk_participation")
	public ParticipationVO getParticipationVO() {
		return participationVO;
	}
	
	@Enumerated(EnumType.STRING)
	public EProcessingState getProcessingState() {
		return processingState;
	}
	
	

	/**
	 * Conta o tempo total de processamento deste payload de imagem
	 * @return
	 */
	@Transient
	public Long getTimeSpent(){
		
		if(getProcessingState().equals(EProcessingState.TERMINATED)){
			
			long lIni = getDtInc().getTime();
			long lAlt = getDtAlt().getTime();
			
			return lAlt - lIni;
			
		}else{
			return 0l;
		}

	}
	
	
	public String getDescricao() {
		return descricao;
	}

	public String getMessageState() {
		return messageState;
	}
	
	@Lob
	public String getFullMessageState() {
		return fullMessageState;
	}
	
	public Long getSize() {
		return size;
	}

	
	public String getPreProcessImgHash() {
		return preProcessImgHash;
	}

	
	@Index(name="idx_pagina_payload")
	public Integer getPage() {
		return page;
	}
	
	/**
	 * @param imageVOs the imageVOs to set
	 */
	public void setImageVOs(List<ImageVO> imageVOs) {
		this.imageVOs = imageVOs;
	}
	

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setPreProcessImgHash(String hash) {
		this.preProcessImgHash = hash;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setParticipationVO(ParticipationVO participationVO) {
		this.participationVO = participationVO;
	}

	public void setMessageState(String stateMessage) {
		this.messageState = stateMessage;
	}

	public void setProcessingState(EProcessingState processingState) {
		this.processingState = processingState;
	}

	public void setFullMessageState(String fullMessageState) {
		this.fullMessageState = fullMessageState;
	}
	
}
