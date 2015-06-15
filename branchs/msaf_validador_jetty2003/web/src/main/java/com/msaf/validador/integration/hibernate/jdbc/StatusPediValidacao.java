package com.msaf.validador.integration.hibernate.jdbc;

import java.io.Serializable;

public class StatusPediValidacao implements Serializable {

	private static final long serialVersionUID = -3832361882653158255L;

	private String idTipoValidacao;
	private String descricaoValidacao;
	private String idTipoResultado;
	private String descricaoResultado;
	private String labelValidacao; 
	private String labelResultado;
	private Integer qtTotal;
	private Integer totalRegistrosProcessado;
	
	
	
	public Integer getTotalRegistrosProcessado() {
		return totalRegistrosProcessado;
	}
	public void setTotalRegistrosProcessado(Integer totalRegistrosProcessado) {
		this.totalRegistrosProcessado = totalRegistrosProcessado;
	}
	public String getLabelValidacao() {
		return descricaoValidacao;
	}
	public String getLabelResultado() {
		return descricaoResultado;
	}
	public String getIdTipoValidacao() {
		return idTipoValidacao;
	}
	public void setIdTipoValidacao(String idTipoValidacao) {
		this.idTipoValidacao = idTipoValidacao;
	}
	public String getDescricaoValidacao() {
		return descricaoValidacao;
	}
	public void setDescricaoValidacao(String descricaoValidacao) {
		this.descricaoValidacao = descricaoValidacao;
	}
	public String getIdTipoResultado() {
		return idTipoResultado;
	}
	public void setIdTipoResultado(String idTipoResultado) {
		this.idTipoResultado = idTipoResultado;
	}
	public String getDescricaoResultado() {
		return descricaoResultado;
	}
	public void setDescricaoResultado(String descricaoResultado) {
		this.descricaoResultado = descricaoResultado;
	}
	public Integer getQtTotal() {
		return qtTotal;
	}
	public void setQtTotal(Integer qtTotal) {
		this.qtTotal = qtTotal;
	}
	
	
}
