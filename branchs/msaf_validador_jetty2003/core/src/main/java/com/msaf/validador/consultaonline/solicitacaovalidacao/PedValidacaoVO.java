package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.msaf.validador.consultaonline.cliente.AbstractVO;
import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consumer.util.DateUtil;

@XmlType
@Entity
public class PedValidacaoVO extends AbstractVO implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PK_GEN_PEDIDO_VALIDACAO")
    @SequenceGenerator(name="PK_GEN_PEDIDO_VALIDACAO", sequenceName="SEQUENCE_PEDIDO_VALIDACAO", allocationSize=1)
	@Column
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_DOWNLOAD")
	private Date dataDownload;

	@OneToMany (fetch=FetchType.EAGER)
	private List<TpValidVO> tpValidacoes;	
	
	@Transient
	private String listaValidacaoFormatada = new String();


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_SOLICITACAO")
	//private String dataSolicitacao;
	private Date dataSolicitacao;
	
	

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_TERMINO")
	private Date dataTermino;

	@Column(name="QTDE_REGISTROS_ARQ")
	private Integer qtdeRegistrosArq;

	@Column(name="QTDE_REGISTROS_PROC")
	private BigDecimal qtdeRegistrosProc;

	@ManyToOne(fetch = FetchType.EAGER)
	//@JoinColumns({ @JoinColumn(name = "CLIENTEFK_IDCLIENTE")}) 
	private ClienteVO clienteFk;

//	@OneToMany(mappedBy="pedValidFk")
	//@OneToMany
	//@OneToMany(cascade = CascadeType.REFRESH)
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="pedValidFk", fetch=FetchType.EAGER)
	private Set<PessoaVO> pessoaFK;
		
	
	private static final long serialVersionUID = 1L;

	
	public PedValidacaoVO() {}

	public PedValidacaoVO(Long id) {
		this.id = id;
	}

	public PedValidacaoVO(ClienteVO clienteFk, Date dateSystem,
			Integer total) {
		this.clienteFk = clienteFk;
		this.dataSolicitacao = DateUtil.getDateSystem();
		//this.dataSolicitacao = DateUtil.format(new Date(), DateUtil.FORMATACAO_DATA_HORA_MINUTO_SEGUNDO);
		this.qtdeRegistrosArq = total;
	}

	public PedValidacaoVO(long l) {
		this.id = l;
	}

	
	
	public List<TpValidVO> getTpValidacoes() { 
		return tpValidacoes;
	}

	public void setTpValidacoes(List<TpValidVO> tpValidacoes) {
		this.tpValidacoes = tpValidacoes;
	}
	
	
	@XmlAttribute(required=false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(nillable=false, required=true)
	public Date getDataDownload() {
		return this.dataDownload;
	}

	public void setDataDownload(Date dataDownload) {
		this.dataDownload = dataDownload;
	}


//	public String getDataSolicitacao() {
//		return this.dataSolicitacao;
//	}
	
	@XmlElement(nillable=false, required=true)
	public Date getDataSolicitacao() {
		return this.dataSolicitacao;
	}

//	public void setDataSolicitacao(String dataSolicitacao) {
//		this.dataSolicitacao = dataSolicitacao;
//	}

	
	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	@XmlElement(nillable=false, required=true)
	public Date getDataTermino() {
		return this.dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	@XmlElement(nillable=false, required=true)
	public Integer getQtdeRegistrosArq() {
		return this.qtdeRegistrosArq;
	}

	public void setQtdeRegistrosArq(Integer qtdReg) {
		this.qtdeRegistrosArq = qtdReg;
	}

	@XmlElement(nillable=false, required=true)
	public BigDecimal getQtdeRegistrosProc() {
		return this.qtdeRegistrosProc;
	}

	public void setQtdeRegistrosProc(BigDecimal qtdeRegistrosProc) {
		this.qtdeRegistrosProc = qtdeRegistrosProc;
	}

	public ClienteVO getClienteFk() {
		return this.clienteFk;
	}

	public void setClienteFk(ClienteVO clienteFk) {
		this.clienteFk = clienteFk;
	}

	@XmlElement(required=true, nillable=false)
	public Set<PessoaVO> getPessoaFK() {
		return pessoaFK;
	}

	public void setPessoaFK(Set<PessoaVO> pessoaFK) {
		this.pessoaFK = pessoaFK;
	}

	/**
	 * Tratamento para apresentar ao usuario uma lista 
	 * de tpos de validacoes a ser apresentadas ao usuario.
	 * @return
	 */
	public String getListaValidacaoFormatada() {
		
		if((this.tpValidacoes != null) && (!this.tpValidacoes.isEmpty())) {
			listaValidacaoFormatada = "";
			for (TpValidVO tipoValidacao : tpValidacoes) {
				listaValidacaoFormatada+=" "+ tipoValidacao.getDescricao();
			}
		}
		return listaValidacaoFormatada;
	}

	
	public void setListaValidacaoFormatada(String listaValidacaoFormatada) {
		this.listaValidacaoFormatada = listaValidacaoFormatada;
	}

//	@XmlElement(required=true, nillable=false)
//	public Set<PessoaVO> getPedValidFk() {
//		return pedValidFk;
//	}
//
//	public void setPedValidFk(Set<PessoaVO> pedValidFk) {
//		this.pedValidFk = pedValidFk;
//	}

//	@XmlElement(required=true, nillable=false)
//	public Set<PessoaVO> getRegistroPessoaCollection() {
//		return this.registroPessoaCollection;
//	}
//
//	public void setRegistroPessoaCollection(Set<PessoaVO> registroPessoaCollection) {
//		this.registroPessoaCollection = registroPessoaCollection;
//	}
	
	

}
