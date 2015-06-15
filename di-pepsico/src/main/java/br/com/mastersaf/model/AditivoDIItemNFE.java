package br.com.mastersaf.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import br.com.mastersaf.util.Bean;

@Entity
@Table(name = "NFE_DI_ADICAO")
public class AditivoDIItemNFE  implements Bean {

	private static final long serialVersionUID = 7076541958418764830L;
	
	@EmbeddedId
	private AditivoDIItemNFEPK aditivoDIItemNFEPK;
	
	@Column(name="NR_SEQ")
	private Long nrSeq;

	@Column(name="CD_FABRICANTE")
	private String fabricante; 

	@Column(name="VL_DESCONTO", scale = 2)
	private BigDecimal desconto;
	
	public AditivoDIItemNFE() {
		this.aditivoDIItemNFEPK = new AditivoDIItemNFEPK();
	}

	public Long getNrSeq() {
		return nrSeq;
	}
	public void setNrSeq(Long nrSeq) {
		this.nrSeq = nrSeq;
	}
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	public void setAditivoDIItemNFEPK(AditivoDIItemNFEPK aditivoDIItemNFEPK) {
		this.aditivoDIItemNFEPK = aditivoDIItemNFEPK;
	}
	public AditivoDIItemNFEPK getAditivoDIItemNFEPK() {
		return aditivoDIItemNFEPK;
	} 
	
	

}
