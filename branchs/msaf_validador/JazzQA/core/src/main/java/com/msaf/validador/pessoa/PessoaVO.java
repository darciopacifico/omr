package com.msaf.validador.pessoa;

import java.util.HashSet;
import java.util.Set;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;
import br.com.dlp.validador.pedvalidacao.PedValidacaoVO;



/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

public class PessoaVO  extends AbstractCadastroBaseVO {

	
	private String bairro;

	
	private String cep;

	
	private String cidade;

	
	private String cnpj;
	
	
	private String complemento;

	private String dataBaixa;

	private String dataConsulta;

	private String dataInclusao;

	private String enquandramentoEmpresa;

	
	private String estado;

	
	private String ie;

	private String iesEncontradas;

	
	private String logradouro;

	
	private String numero;

	private String numeroConsulta;

	private String qtdeEncontrada;

	private String razaoSocial;

	private String regimeApuracao;

	
	private String situacao;

	private String identif;

	
	private PedValidacaoVO pedValidacaoVO;

	//@OneToMany(cascade = CascadeType.ALL, mappedBy="registroPessoaFk", targetEntity=DadosRetornoInstituicaoVO.class)
	//@JoinColumn(name="BID_ITEM_ID", referencedColumnName="ITEM_ID")
	//@JoinTable(name="DRI_RP")
	//@JoinColumn(name="REGISTRO_PESSOA_FK")
	//@OneToMany(mappedBy="registroPessoaFk")
	//@OneToMany(cascade = CascadeType.ALL)
	private Set dadosRetInsts = new HashSet();

	private static final long serialVersionUID = 1L;

	public PessoaVO() {
		super();
		this.dadosRetInsts = new HashSet();
	}


	/**
	 * @wj2ee ordem="10" somenteLeitura="true" descricao="Código"
	 *        pesquisavel="true" listavel="true" pk="true"
	 * 
	 * @hibernate.id generator-class="increment"
	 */
	public Integer getId() {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		return abstractJazzQAPK.getId();
	}




	public void setId(Integer id) {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		abstractJazzQAPK.setId(id);
	}

	public String getBairro() {
		return this.bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return this.cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return this.cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getComplemento() {
		return this.complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getDataBaixa() {
		return this.dataBaixa;
	}

	public void setDataBaixa(String dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public String getDataConsulta() {
		return this.dataConsulta;
	}

	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public String getDataInclusao() {
		return this.dataInclusao;
	}

	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getEnquandramentoEmpresa() {
		return this.enquandramentoEmpresa;
	}

	public void setEnquandramentoEmpresa(String enquandramentoEmpresa) {
		this.enquandramentoEmpresa = enquandramentoEmpresa;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIe() {
		return this.ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public String getIesEncontradas() {
		return this.iesEncontradas;
	}

	public void setIesEncontradas(String iesEncontradas) {
		this.iesEncontradas = iesEncontradas;
	}

	public String getLogradouro() {
		return this.logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNumeroConsulta() {
		return this.numeroConsulta;
	}

	public void setNumeroConsulta(String numeroConsulta) {
		this.numeroConsulta = numeroConsulta;
	}

	public String getQtdeEncontrada() {
		return this.qtdeEncontrada;
	}

	public void setQtdeEncontrada(String qtdeEncontrada) {
		this.qtdeEncontrada = qtdeEncontrada;
	}

	public String getRazaoSocial() {
		return this.razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getRegimeApuracao() {
		return this.regimeApuracao;
	}

	public void setRegimeApuracao(String regimeApuracao) {
		this.regimeApuracao = regimeApuracao;
	}

	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	
	public String getIdentif() {
		return identif;
	}

	public void setIdentif(String identif) {
		this.identif = identif;
	}

	public PedValidacaoVO getPedValidFk() {
		return this.pedValidacaoVO;
	}
	
	
	public void setPedValidFk(PedValidacaoVO pedValidFk) {
		this.pedValidacaoVO = pedValidFk;
	}

	public Set getDadosRetInsts() {
		return this.dadosRetInsts  = new HashSet();
	}

	public void setDadosRetInsts(Set dadosRetornoInstituicaoCollection) {
		this.dadosRetInsts = dadosRetornoInstituicaoCollection;
	}

	public Set getDadosRetorno() {
		return dadosRetInsts;
	}

	public void setDadosRetorno(Set dadosRetorno) {
		this.dadosRetInsts = dadosRetorno;
	}
}
