package br.com.mastersaf.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.mastersaf.util.Bean;
import br.com.mastersaf.util.DateUtil;


@Entity
@Table(name="NFE_NF")
public class NotaFiscalEletronica implements Bean {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NotaFiscalEletronicaPK notaFiscalEletronicaPK;
	
	@Column(name="NR_SERIE", updatable = false, insertable = false)
	private Long serie;
	
	@Column(name="NR_DOCUMENTO_FISCAL", updatable = false, insertable = false)
	private Long nrDocNf;
	
	@Column(name="DT_EMISSAO", updatable = false, insertable = false)
	private Date emissao;
	
	@Column(name="VL_NF", updatable = false, insertable = false, scale = 2 )
	private BigDecimal valor;
	
	@Column(name = "DM_ST_PROC")
	private Long dmStProc;
	
	@Column(name = "DM_OPER")
	private Long dmOper;
	
	
	public NotaFiscalEletronica(){
		this.notaFiscalEletronicaPK = new NotaFiscalEletronicaPK();
	}
	
	@Transient
	private String emissaoFormat;
	
	@Transient
	private String valorFormat;
	
	@Transient
	private Integer maxItem;
	
	@Transient
	private Date dataEmissaoInicio;
	
	@Transient
	private Date dataEmissaoFinal;

	@Transient
	@SuppressWarnings("unused")
	private String dataInicio;
	
	@Transient
	@SuppressWarnings("unused")
	private String empresa;
	
	@Transient
	@SuppressWarnings("unused")
	private String numeroNota;
	
	@Transient
	@SuppressWarnings("unused")
	private String dataFinal;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "notaFiscalEletronica")
	private List<ItemNFE> listItem = new ArrayList<ItemNFE>();
	public List<ItemNFE> getListItem() {
		return listItem;
	}
	
	@Transient
	private Long gtdItens;
	public void setGtdItens(Long gtdItens) {
		if(this.listItem != null){
			this.gtdItens = Long.valueOf(this.listItem.size());
		}
		this.gtdItens = gtdItens;
	}
	public Long getGtdItens() {
		if(this.listItem != null){
			this.gtdItens = Long.valueOf(this.listItem.size());
		}
		return gtdItens;
	}

	public void setListItem(List<ItemNFE> listItem) {
		this.listItem = listItem;
	}

	public String getNumeroNota() {
		if(null==nrDocNf) return "";
		return nrDocNf.toString() ;
	}

	public void setNumeroNota(String numeroNota) {
		this.nrDocNf = Long.parseLong(numeroNota);
		this.numeroNota = numeroNota;
	}


	public String getDataInicio() {
		return DateUtil.formatStruts(dataEmissaoInicio);
	}
	
	public Long getNrDocNf() {
		return nrDocNf;
	}

	public void setNrDocNf(Long nrDocNf) {
		this.nrDocNf = nrDocNf;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
		dataEmissaoInicio =DateUtil.formatStruts(dataInicio);
	}
	public String getDataFinal() {
		return DateUtil.formatStruts(dataEmissaoFinal);
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
		dataEmissaoFinal =DateUtil.formatStruts(dataFinal);
	}

	public Date getDataEmissaoInicio() {
		return dataEmissaoInicio;
	}

	public void setDataEmissaoInicio(Date dataEmissaoInicio) {
		this.dataEmissaoInicio = dataEmissaoInicio;
	}

	public Date getDataEmissaoFinal() {
		return dataEmissaoFinal;
	}

	public void setDataEmissaoFinal(Date dataEmissaoFinal) {
		this.dataEmissaoFinal = dataEmissaoFinal;
	}

	public Long getSerie() {
		return serie;
	}

	public void setSerie(Long serie) {
		this.serie = serie;
	}

	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emisao) {
		this.emissao = emisao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setMaxItem(Integer maxItem) {
		this.maxItem = maxItem;
	}

	public Integer getMaxItem() {
		return maxItem;
	}
	
	public Long getDmStProc() {
		return dmStProc;
	}

	public void setDmStProc(Long dmStProc) {
		this.dmStProc = dmStProc;
	}

	public Long getDmOper() {
		return dmOper;
	}

	public void setDmOper(Long dmOper) {
		this.dmOper = dmOper;
	}
	public void setEmissaoFormat(String emissaoFormat) {
		this.emissaoFormat = emissaoFormat;
	}
	public String getEmissaoFormat() {
		return DateUtil.formatStruts(emissao);
	}
	public void setValorFormat(String valorFormat) {
		this.valorFormat = valorFormat;
	}
	public String getValorFormat() {
		String valorString = String.valueOf(valor);
		return "R$ " + valorString.replace(".", ",");
	}
	public void setNotaFiscalEletronicaPK(NotaFiscalEletronicaPK notaFiscalEletronicaPK) {
		this.notaFiscalEletronicaPK = notaFiscalEletronicaPK;
	}
	public NotaFiscalEletronicaPK getNotaFiscalEletronicaPK() {
		return notaFiscalEletronicaPK;
	}



	

}
