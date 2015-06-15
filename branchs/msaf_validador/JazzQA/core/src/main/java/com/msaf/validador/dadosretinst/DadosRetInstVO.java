package com.msaf.validador.dadosretinst;



import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;

import com.msaf.validador.pessoa.PessoaVO;
import com.msaf.validador.tpresult.TpResultVO;
import com.msaf.validador.tpvalid.TpValidVO;


/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

public class DadosRetInstVO extends AbstractCadastroBaseVO{
	
	
	private String bairro;

	
	private String cep;

	
	private String cidade;

	
	private String cnpj;

	
	private String complemento;

	// 
	
	//private Date dataBaixa;
	private String dataBaixa;
	
	// 
	
	//private Date dataConsulta;
	private String dataConsulta;

	// 
	
	//private Date dataInclusao;
	private String dataInclusao;

	
	private String enquandramentoEmpresa;

	
	private String estado;

	
	private String ie;

	
	//private BigDecimal iesEncontradas;
	private String iesEncontradas;

	
	private String logradouro;

	
	private String numero;

	
	//private BigDecimal numeroConsulta;
	private String numeroConsulta;

	
	//private BigDecimal qtdeEncontrada;
	private String qtdeEncontrada;

	private String razaoSocial;

	private String regimeApuracao;

	private String situacao;

	private String identif;
	
	private String tipoDocumento;
	
	
	//(cascade = { CascadeType.PERSIST, CascadeType.REFRESH } )
	//(cascade = { CascadeType.ALL })
	private PessoaVO pessoaVO;


	//@OneToOne(cascade = { CascadeType.ALL })
	
	private TpResultVO tpResultVO;
	
	
	private TpValidVO tpValidVO;

	private static final long serialVersionUID = 1L;

	public DadosRetInstVO() {
		super();
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

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getBairro() {
		return this.bairro;
	}

	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getCep() {
		return this.cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getCidade() {
		return this.cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getComplemento() {
		return this.complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	//public Date getDataBaixa() {
	public String getDataBaixa() {
		return this.dataBaixa;
	}

	
	//public void setDataBaixa(Date dataBaixa) {
	public void setDataBaixa(String dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	//public Date getDataConsulta() {
	public String getDataConsulta() {
		return this.dataConsulta;
	}

	//public void setDataConsulta(Date dataConsulta) {
	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	//public Date getDataInclusao() {
	public String getDataInclusao() {
		return this.dataInclusao;
	}

	//public void setDataInclusao(Date dataInclusao) {
	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getEnquandramentoEmpresa() {
		return this.enquandramentoEmpresa;
	}

	public void setEnquandramentoEmpresa(String enquandramentoEmpresa) {
		this.enquandramentoEmpresa = enquandramentoEmpresa;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getIe() {
		return this.ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getIesEncontradas() {
		return this.iesEncontradas;
	}

	public void setIesEncontradas(String iesEncontradas) {
		this.iesEncontradas = iesEncontradas;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getLogradouro() {
		return this.logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	//public BigDecimal getNumeroConsulta() {
	public String getNumeroConsulta() {
		return this.numeroConsulta;
	}

	//public void setNumeroConsulta(BigDecimal numeroConsulta) {
	public void setNumeroConsulta(String numeroConsulta) {
		this.numeroConsulta = numeroConsulta;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	//public BigDecimal getQtdeEncontrada() {
	public String getQtdeEncontrada() {
		return this.qtdeEncontrada;
	}

	//public void setQtdeEncontrada(BigDecimal qtdeEncontrada) {
	public void setQtdeEncontrada(String qtdeEncontrada) {
		this.qtdeEncontrada = qtdeEncontrada;
	}

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getRazaoSocial() {
		return this.razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getRegimeApuracao() {
		return this.regimeApuracao;
	}

	public void setRegimeApuracao(String regimeApuracao) {
		this.regimeApuracao = regimeApuracao;
	}

	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getSituacao() {
		return this.situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public PessoaVO getRegistroPessoaFk() {
		return this.pessoaVO;
	}

	public void setRegistroPessoaFk(PessoaVO registroPessoaFk) {
		this.pessoaVO = registroPessoaFk;
	}

	public TpResultVO getTipoResultadoFk() {
		return this.tpResultVO;
	}

	public void setTipoResultadoFk(TpResultVO tipoResultadoFk) {
		this.tpResultVO = tipoResultadoFk;
	}

	public TpValidVO getTipoValidacaoFk() {
		return this.tpValidVO;
	}

	public void setTipoValidacaoFk(TpValidVO tipoValidacaoFk) {
		this.tpValidVO = tipoValidacaoFk;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getIdentif() {
		return identif;
	}

	public void setIdentif(String identif) {
		this.identif = identif;
	}


	
	


	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	// este atributo nao será convertido em XML.
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	
	/********************** Campos restritos para uso do jaxB *********************/
	// acrescentado a pagina para retornar o valor do XML
	//String pagina = new String();
	
//FIXME:CONVERAR COM O PESSOAL DA QUESTAO DO ATRIBTO pagina, QUE NAO PODE SER MANIPULADO
//AO MENOS QUE SEJA MAPEADO NO JPA.
	
//	(name="Pagina")
//	public String getPagina() {
//		return pagina;
//	}
//
//	public void setPagina(String pagina) {
//		this.pagina = pagina;
//	}
	private String codErro = new String();
	
	private String erro = new String();

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	
	public String getCodErro() {
		return codErro;
	}

	public void setCodErro(String codErro) {
		this.codErro = codErro;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	
	public String getErro() {
		return erro;
	}

	
	public void setErro(String erro) {
		this.erro = erro;
	}


	
	
	
	
	
}
