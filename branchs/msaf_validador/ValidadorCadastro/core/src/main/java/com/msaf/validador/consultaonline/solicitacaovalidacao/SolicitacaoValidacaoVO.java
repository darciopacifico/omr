package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.msaf.validador.consultaonline.cliente.AbstractVO;
import com.msaf.validador.consultaonline.cliente.ClienteVO;

/**
 * Contempla uma solicitação de velidação de cadastro de um cliente. 
 * Contém lista de empresas a serem validadas e tipos de validação 
 * que serão executadas.
 * 
 * Após a execução da validação, esta mesma hierarquia é 
 * utilizada para retornar os resultados da consulta para o cliente Msaf
 * 
 * @author dlopes
 */
@XmlType
@XmlRootElement
@Deprecated
public class SolicitacaoValidacaoVO extends AbstractVO {

	private static final long serialVersionUID = -2307702934626610531L;

	private Integer id;

	private Date dataSolicitacao;
	private Date dataTermino;
	private Date dataDownload;
	
	private ClienteVO clienteVO;
	
	private Collection<PessoaVO> registrosParaValidacao;
	
	private Collection<TpValidVO> tiposValidacao;
	
	
	
	@XmlElement(nillable=false, required=true)
	public ClienteVO getClienteVO() {
		return clienteVO;
	}
	
	@XmlElement(required=false)
	public Date getDataDownload() {
		return dataDownload;
	}
	
	@XmlElement(required=false)
	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}
	@XmlElement(required=false)
	public Date getDataTermino() {
		return dataTermino;
	}
	@XmlAttribute(required=false)
	public Integer getId() {
		return id;
	}
	
	@XmlElement(required=true, nillable=false)
	public Collection<PessoaVO> getRegistrosParaValidacao() {
		return registrosParaValidacao;
	}
	
	@XmlElement(required=true, nillable=false)
	public Collection<TpValidVO> getTiposValidacao() {
		return tiposValidacao;
	}
	
	
	public void setClienteVO(ClienteVO clienteVO) {
		this.clienteVO = clienteVO;
	}
	public void setDataDownload(Date dataDownload) {
		this.dataDownload = dataDownload;
	}
	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setRegistrosParaValidacao(Collection<PessoaVO> registrosParaValidacao) {
		this.registrosParaValidacao = registrosParaValidacao;
	}
	
	public void setTiposValidacao(Collection<TpValidVO> tiposValidacao) {
		this.tiposValidacao = tiposValidacao;
	}
	/*
	public boolean equals(Object obj) {
		if (obj instanceof SolicitacaoValidacaoVO == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		SolicitacaoValidacaoVO solicitacaoValidacaoVO = (SolicitacaoValidacaoVO) obj;
		return new EqualsBuilder()
		.appendSuper(super.equals(obj))
		.append(id, solicitacaoValidacaoVO.id)
		.isEquals();
		
	}
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
		append(id).
		toHashCode();
	}*/
}
