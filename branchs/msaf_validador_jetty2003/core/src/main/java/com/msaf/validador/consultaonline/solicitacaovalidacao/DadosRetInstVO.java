package com.msaf.validador.consultaonline.solicitacaovalidacao;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.msaf.validador.consultaonline.cliente.AbstractVO;


@Entity
@XmlType(name="Registro")
public class DadosRetInstVO extends AbstractVO {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PK_GEN_RET_INST")
    @SequenceGenerator(name="PK_GEN_RET_INST", sequenceName="SEQUENCE_DADOS_RET_INST", allocationSize=1)
	@Column
	private long id;

	//campos para consulta receita pessoa física
	@Column
  private String cpf;
  
	@Column
  private String situacaoCadastral;

	@Column
  private String codigoConsulta;
	
	@Column
  private String nome;


  
  @Column
	private String bairro;

	@Column
	private String cep;

	@Column
	private String cidade;

	@Column
	private String cnpj;

	@Column
	private String complemento;

	@Column
	private String dataBaixa;
	 
	@Column
	private String dataConsulta;
 
	@Column
	private String dataInclusao;

	@Column
	private String enquandramentoEmpresa;

	@Column
	private String estado;

	@Column
	private String ie;
	
	/**********Campos do Sumafra**********/
	@Column
	private String ie2;
	
	@Column
	private String sumafra;
	
	@Column
	private String dataInscSuframa;
	
	@Column
	private String incentivo;
	
	@Column
	private String dataValidade;
	
	@Column
	private String atividadeEconomica;
	
	@Column
	private String tipoIncentivo;
	
	@Column
	private String tsentaTSA;
	/***************************************/
	
	
	
	/**********Campos do CRM**********/
	@Column
	private String CRM;
	
	@Column
	private String tipoInscricao;
	
	@Column
	private String especialidade;
	/***************************************/


	/**********Campos do Municipios IBGE**********/
	@Column
	private String codigoIBGE;
	
	//@Column
	//private String municipio;
	/***************************************/
	
	/**********Campos do ANP**********/
	//@Column
	private String nomeFantasia;

	//@Column
	//private String endereco;
	
	@Column
	private String bandeiraPosto;
	
	@Column
	private String autorizacao;

	@Column
	private String dataPublicacao;

	@Column
	private String tipoPosto;
	
	@Column
	private String horaConsulta;
	/***************************************/
	
	
	/**********Campos do RECEITA FEDERAL**********/
	@Column
	private String dataAbertura;
	
	@Column
	private String codigoAE;
	
	@Column
	private String descricaoAE;

	@Column
	private String codigoNJ;
	
	@Column
	private String descricaoNJ;
	
	@Column
	private String dataSituacao;
	
	@Column
	private String situacaoEspecial;

	@Column
	private String dataSituacaoEspecial;

	/*********************************************/

	@Column
	private String iesEncontradas;

	@Column
	private String logradouro;

	@Column
	private String numero;

	@Column
	//private BigDecimal numeroConsulta;
	private String numeroConsulta;


	
	
	@Column
	//private BigDecimal qtdeEncontrada;
	private String qtdeEncontrada;

	@Column(name="RAZAO_SOCIAL")
	private String razaoSocial;

	@Column(name="REGIME_APURACAO")
	private String regimeApuracao;

	@Column(name="SITUACAO")
	private String situacao;

	@Column(name="IDENTIF")
	private String identif;
	

	@Column(name="TIPO_DOCUMENTO")
	private String tipoDocumento;
	
	@Lob 
	@Column(name="PAGINA")
	private String pagina;
	
	
	 
	//@ManyToOne
	//@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH } )

	//@OneToOne(cascade = { CascadeType.ALL })
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private TpResultVO tipoResultadoFk;
	
	@ManyToOne
	private TpValidVO tipoValidacaoFk;

	private static final long serialVersionUID = 1L;

	public DadosRetInstVO() {
		super();
	}

	

	@XmlElement(name="Bairro")
	public String getBairro() {
		return this.bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@XmlElement(name="CEP")
	public String getCep() {
		return this.cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@XmlElement(name="Cidade")
	public String getCidade() {
		return this.cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@XmlElement(name="CNPJ")
	public String getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@XmlElement(name="Complemento")
	public String getComplemento() {
		return this.complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@XmlElement(name="DataBaixa")
	//public Date getDataBaixa() {
	public String getDataBaixa() {
		return this.dataBaixa;
	}

	
	//public void setDataBaixa(Date dataBaixa) {
	public void setDataBaixa(String dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	@XmlElement(name="DataConsulta")
	//public Date getDataConsulta() {
	public String getDataConsulta() {
		return this.dataConsulta;
	}

	//public void setDataConsulta(Date dataConsulta) {
	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	@XmlElement(name="DataInclusao")
	//public Date getDataInclusao() {
	public String getDataInclusao() {
		return this.dataInclusao;
	}

	//public void setDataInclusao(Date dataInclusao) {
	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	@XmlElement(name="EnquadramentoEmpresa")
	public String getEnquandramentoEmpresa() {
		return this.enquandramentoEmpresa;
	}

	public void setEnquandramentoEmpresa(String enquandramentoEmpresa) {
		this.enquandramentoEmpresa = enquandramentoEmpresa;
	}

	@XmlElement(name="Estado")
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@XmlElement(name="IE")
	public String getIe() {
		return this.ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	@XmlElement(name="IEsEncontradas")
	//public BigDecimal getIesEncontradas() {
	public String getIesEncontradas() {
		return this.iesEncontradas;
	}

	//public void setIesEncontradas(BigDecimal iesEncontradas) {
	public void setIesEncontradas(String iesEncontradas) {
		this.iesEncontradas = iesEncontradas;
	}

	@XmlElement(name="Logradouro")
	public String getLogradouro() {
		return this.logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	@XmlElement(name="Numero")
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@XmlElement(name="NumeroConsulta")
	//public BigDecimal getNumeroConsulta() {
	public String getNumeroConsulta() {
		return this.numeroConsulta;
	}

	//public void setNumeroConsulta(BigDecimal numeroConsulta) {
	public void setNumeroConsulta(String numeroConsulta) {
		this.numeroConsulta = numeroConsulta;
	}

	@XmlElement(name="QtdEncontrada")
	//public BigDecimal getQtdeEncontrada() {
	public String getQtdeEncontrada() {
		return this.qtdeEncontrada;
	}

	//public void setQtdeEncontrada(BigDecimal qtdeEncontrada) {
	public void setQtdeEncontrada(String qtdeEncontrada) {
		this.qtdeEncontrada = qtdeEncontrada;
	}

	@XmlElement(name="RazaoSocial")
	public String getRazaoSocial() {
		return this.razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@XmlElement(name="RegimeApuracao")
	public String getRegimeApuracao() {
		return this.regimeApuracao;
	}

	public void setRegimeApuracao(String regimeApuracao) {
		this.regimeApuracao = regimeApuracao;
	}

	@XmlElement(name="Situacao")
	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public TpResultVO getTipoResultadoFk() {
		return this.tipoResultadoFk;
	}

	public void setTipoResultadoFk(TpResultVO tipoResultadoFk) {
		this.tipoResultadoFk = tipoResultadoFk;
	}

	public TpValidVO getTipoValidacaoFk() {
		return this.tipoValidacaoFk;
	}

	public void setTipoValidacaoFk(TpValidVO tipoValidacaoFk) {
		this.tipoValidacaoFk = tipoValidacaoFk;
	}

	@XmlAttribute(name="Id")
	public String getIdentif() {
		return identif;
	}

	public void setIdentif(String identif) {
		this.identif = identif;
	}

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	// este atributo nao será convertido em XML.
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}



	
	
	@XmlElement(name="IE2")
	public String getIe2() {
		return ie2;
	}



	public void setIe2(String ie2) {
		this.ie2 = ie2;
	}


	@XmlElement(name="Suframa")
	public String getSumafra() {
		return sumafra;
	}



	public void setSumafra(String sumafra) {
		this.sumafra = sumafra;
	}


	
	@XmlElement(name="DataInscSuframa")
	public String getDataInscSuframa() {
		return dataInscSuframa;
	}



	public void setDataInscSuframa(String dataInscSuframa) {
		this.dataInscSuframa = dataInscSuframa;
	}


	
	@XmlElement(name="Incentivo")
	public String getIncentivo() {
		return incentivo;
	}



	public void setIncentivo(String incentivo) {
		this.incentivo = incentivo;
	}


	
	@XmlElement(name="AtividadeEconomica")
	public String getAtividadeEconomica() {
		return atividadeEconomica;
	}



	public void setAtividadeEconomica(String atividadeEconomica) {
		this.atividadeEconomica = atividadeEconomica;
	}


	
	@XmlElement(name="TipoIncentivo")
	public String getTipoIncentivo() {
		return tipoIncentivo;
	}



	public void setTipoIncentivo(String tipoIncentivo) {
		this.tipoIncentivo = tipoIncentivo;
	}


	@XmlElement(name="DataValidade")
	public String getDataValidade() {
		return dataValidade;
	}


	public void setDataValidade(String dataValidade) {
		this.dataValidade = dataValidade;
	}

	
	@XmlElement(name="IsentaTSA")
	public String getTsentaTSA() {
		return tsentaTSA;
	}



	public void setTsentaTSA(String tsentaTSA) {
		this.tsentaTSA = tsentaTSA;
	}



	
	@XmlElement(name="CRM")
	public String getCRM() {
		return CRM;
	}



	public void setCRM(String crm) {
		CRM = crm;
	}


	@XmlElement(name="TipoInscricao")
	public String getTipoInscricao() {
		return tipoInscricao;
	}


	public void setTipoInscricao(String tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}


	@XmlElement(name="Especialidade")
	public String getEspecialidade() {
		return especialidade;
	}



	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}



	@XmlElement(name="CodigoIBGE")
	public String getCodigoIBGE() {
		return codigoIBGE;
	}



	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}


	
	@XmlElement(name="Municipio")
	public String getMunicipio() {
		//return municipio;
		return cidade;
	}

	public void setMunicipio(String municipio) {
		//this.municipio = municipio;
		// seta cidade tbem com 
		this.cidade = municipio;
	}


	@XmlElement(name="NomeFantasia")
	public String getNomeFantasia() {
		return nomeFantasia;
	}



	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}


	@XmlElement(name="Endereco")
	public String getEndereco() {
		//return endereco;
		return logradouro;
	}



	public void setEndereco(String endereco) {
		//this.endereco = endereco;
		this.logradouro = endereco;
	}


	@XmlElement(name="BandeiraPosto")
	public String getBandeiraPosto() {
		return bandeiraPosto;
	}



	public void setBandeiraPosto(String bandeiraPosto) {
		this.bandeiraPosto = bandeiraPosto;
	}

	
	@XmlElement(name="Autorizacao")
	public String getAutorizacao() {
		return autorizacao;
	}



	public void setAutorizacao(String autorizacao) {
		this.autorizacao = autorizacao;
	}


	@XmlElement(name="DataPublicacao")
	public String getDataPublicacao() {
		return dataPublicacao;
	}



	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}


	@XmlElement(name="TipoPosto")
	public String getTipoPosto() {
		return tipoPosto;
	}



	public void setTipoPosto(String tipoPosto) {
		this.tipoPosto = tipoPosto;
	}


	@XmlElement(name="HoraConsulta")
	public String getHoraConsulta() {
		return horaConsulta;
	}



	public void setHoraConsulta(String horaConsulta) {
		this.horaConsulta = horaConsulta;
	}

	
	
	
	
	
	
	
	
	
	
	/********************** Campos restritos para uso do jaxB *********************/
	// acrescentado a pagina para retornar o valor do XML
	//String pagina = new String();
	
//FIXME:CONVERAR COM O PESSOAL DA QUESTAO DO ATRIBTO pagina, QUE NAO PODE SER MANIPULADO
//AO MENOS QUE SEJA MAPEADO NO JPA.
	
	@XmlElement(name="Pagina")
	public String getPagina() {
		//return pagina;
		return null;
	}

	public void setPagina(String pagina) {
		//this.pagina = pagina;
	}


	@XmlElement(name="DataAbertura")
	public String getDataAbertura() {
		return dataAbertura;
	}



	public void setDataAbertura(String dataAbertura) {
		this.dataAbertura = dataAbertura;
	}


	@XmlElement(name="CodigoAE")
	public String getCodigoAE() {
		return codigoAE;
	}



	public void setCodigoAE(String codigoAE) {
		this.codigoAE = codigoAE;
	}


	@XmlElement(name="DescricaoAE")
	public String getDescricaoAE() {
		return descricaoAE;
	}



	public void setDescricaoAE(String descricaoAE) {
		this.descricaoAE = descricaoAE;
	}


	@XmlElement(name="CodigoNJ")
	public String getCodigoNJ() {
		return codigoNJ;
	}



	public void setCodigoNJ(String codigoNJ) {
		this.codigoNJ = codigoNJ;
	}


	@XmlElement(name="DescricaoNJ")
	public String getDescricaoNJ() {
		return descricaoNJ;
	}



	public void setDescricaoNJ(String descricaoNJ) {
		this.descricaoNJ = descricaoNJ;
	}


	@XmlElement(name="DataSituacao")
	public String getDataSituacao() {
		return dataSituacao;
	}



	public void setDataSituacao(String dataSituacao) {
		this.dataSituacao = dataSituacao;
	}


	@XmlElement(name="SituacaoEspecial")
	public String getSituacaoEspecial() {
		return situacaoEspecial;
	}



	public void setSituacaoEspecial(String situacaoEspecial) {
		this.situacaoEspecial = situacaoEspecial;
	}


	@XmlElement(name="DataSituacaoEspecial")
	public String getDataSituacaoEspecial() {
		return dataSituacaoEspecial;
	}



	public void setDataSituacaoEspecial(String dataSituacaoEspecial) {
		this.dataSituacaoEspecial = dataSituacaoEspecial;
	}



	/**
	 * @return the cpf
	 */
	
  @XmlElement(name="CPF")
	public String getCpf() {
		return cpf;
	}



	/**
	 * @return the situacaoCadastral
	 */
  @XmlElement(name="SituacaoCadastral")
	public String getSituacaoCadastral() {
		return situacaoCadastral;
	}



	/**
	 * @return the codigoConsulta
	 */
  @XmlElement(name="CodigoConsulta")
	public String getCodigoConsulta() {
		return codigoConsulta;
	}



	/**
	 * @return the nome
	 */
  @XmlElement(name="Nome")
	public String getNome() {
		return nome;
	}



	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	/**
	 * @param situacaoCadastral the situacaoCadastral to set
	 */
	public void setSituacaoCadastral(String situacaoCadastral) {
		this.situacaoCadastral = situacaoCadastral;
	}



	/**
	 * @param codigoConsulta the codigoConsulta to set
	 */
	public void setCodigoConsulta(String codigoConsulta) {
		this.codigoConsulta = codigoConsulta;
	}



	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
