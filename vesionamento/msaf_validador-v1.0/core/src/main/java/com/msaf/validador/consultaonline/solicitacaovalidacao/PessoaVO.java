package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class PessoaVO implements Serializable {

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PK_GEN_REG_PESSOA")
    @SequenceGenerator(name="PK_GEN_REG_PESSOA", sequenceName="SEQUENCE_REG_PESSOA", allocationSize=1)
	@Column
	private long id;

	@Column
	private String bairro;

	@Column
	private String cep;

	@Column
	private String cidade;

	@Column
	private String cnpj;
	
	@Column
	private String cpf;
	
	@Column
	private String complemento;

	@Column(name="DATA_BAIXA")
	private String dataBaixa;

	@Column(name="DATA_CONSULTA")
	private String dataConsulta;

	@Column(name="DATA_INCLUSAO")
	private String dataInclusao;

	@Column(name="ENQUANDRAMENTO_EMPRESA")
	private String enquandramentoEmpresa;

	@Column
	private String estado;

	@Column
	private String ie;

	@Column(name="IES_ENCONTRADAS")
	private String iesEncontradas;

	@Column
	private String logradouro;

	@Column
	private String numero;

	@Column(name="NUMERO_CONSULTA")
	private String numeroConsulta;

	@Column(name="QTDE_ENCONTRADA")
	private String qtdeEncontrada;

	@Column(name="RAZAO_SOCIAL")
	private String razaoSocial;

	@Column(name="REGIME_APURACAO")
	private String regimeApuracao;

	@Column
	private String situacao;

	@Column(name="IDENTIF")
	private String identif;

	//@ManyToOne(cascade = CascadeType.ALL )
	@ManyToOne
	@JoinColumn(name="PEDVALIDFK_ID",
	referencedColumnName="ID", updatable=true, insertable=true)
	private PedValidacaoVO pedValidFk;

	//@OneToMany(cascade = CascadeType.ALL, mappedBy="registroPessoaFk", targetEntity=DadosRetornoInstituicaoVO.class)
	//@JoinColumn(name="BID_ITEM_ID", referencedColumnName="ITEM_ID")
	//@JoinTable(name="DRI_RP")
	//@JoinColumn(name="REGISTRO_PESSOA_FK")
	//@OneToMany(mappedBy="registroPessoaFk")
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<DadosRetInstVO> dadosRetornoFk = new HashSet<DadosRetInstVO>();

	private static final long serialVersionUID = 1L;

	public PessoaVO() {
		super();
		this.setDadosRetornoFk(new HashSet<DadosRetInstVO>());
	}

	public long getId() {
		return this.id;
	}

	public void setId(long idRegistroPessoa) {
		this.id = idRegistroPessoa;
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
		return this.pedValidFk;
	}
	
	
	public void setPedValidFk(PedValidacaoVO pedValidFk) {
		this.pedValidFk = pedValidFk;
	}

	public void setDadosRetornoFk(Set<DadosRetInstVO> dadosRetorno) {
		this.dadosRetornoFk = dadosRetorno;
	}

	public Set<DadosRetInstVO> getDadosRetornoFk() {
		return dadosRetornoFk;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



//	public Set<DadosRetInstVO> getDadosRetornoInstituicaoCollection() {
//		return this.dadosRetorno  = new HashSet<DadosRetInstVO>();
//	}
//
//	public void setDadosRetornoInstituicaoCollection(Set<DadosRetInstVO> dadosRetornoInstituicaoCollection) {
//		this.dadosRetorno = dadosRetornoInstituicaoCollection;
//	}

}
