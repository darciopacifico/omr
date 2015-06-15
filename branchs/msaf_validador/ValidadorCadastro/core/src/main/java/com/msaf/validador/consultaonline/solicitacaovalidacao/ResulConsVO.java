package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.msaf.validador.consultaonline.cliente.AbstractVO;


/**
 * Coleção de resultados da consulta à DLL do Validador (q consulta o site do
 * Sintegra)
 * 
 * @author dlopes
 * 
 */
@XmlType
//@XmlRootElement(name="ResultadosConsulta")
@XmlRootElement(name="ResultadoConsulta")
public class ResulConsVO extends AbstractVO{
	private String versao;
	private Integer quantidade;
	
	//private Collection<DadosRetInstVO> dadosRetornoInstituicaoVOS = new ArrayList<DadosRetInstVO>();
	private Set<DadosRetInstVO> dadosRetornoInstituicaoVOS = new HashSet<DadosRetInstVO>();
	
	@XmlElement(name="Registro")
	public Set<DadosRetInstVO> getRegistrosRetorno() {
		return dadosRetornoInstituicaoVOS;
    }

	
//	public Collection<RegistroPessoaVO> getRegistrosRetorno() {
//		return registrosRetorno;
//	}

	@XmlAttribute(name="Versao")
	public String getVersao() {
		return versao;
	}

	@XmlAttribute(name="Quantidade")
	public Integer getQuantidade() {
		return quantidade;
	}
	
//	public String getRetornoXML() {
//		return retornoXML;
//	}
//
//	public void setRetornoXML(String retornoXML) {
//		this.retornoXML = retornoXML;
//	}
//	
	public void setVersao(String versao) {
		this.versao = versao;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
//	public void setRegistrosRetorno(
//			Collection<RegistroPessoaVO> registrosRetorno) {
//		this.registrosRetorno = registrosRetorno;
//	}

	
	//public void setRegistrosRetorno(Collection<DadosRetInstVO> dadosRetornoInstituicaoVOs) {
	public void setRegistrosRetorno(Set<DadosRetInstVO> dadosRetornoInstituicaoVOs) {
		this.dadosRetornoInstituicaoVOS = dadosRetornoInstituicaoVOs;
	}
	
	@Transient
	private String codErro = new String();
	
	@Transient
	private String erro = new String();

	@XmlElement(name="CodErro")
	public String getCodErro() {
		return codErro;
	}

	public void setCodErro(String codErro) {
		this.codErro = codErro;
	}

	@XmlElement(name="Erro")
	public String getErro() {
		return erro;
	}

	
	public void setErro(String erro) {
		this.erro = erro;
	}


}
