package br.com.mastersaf.model;

import javax.persistence.AttributeOverride;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.mastersaf.util.Bean;

@Entity
@Table(name = "NFE_NF_ITEM")
public class ItemNFE implements Bean{

	private static final long serialVersionUID = -8989322457559977666L;
	
	@EmbeddedId
	private ItemNFEPK itemNFEPK;
	
	public ItemNFE(){
		this.itemNFEPK = new ItemNFEPK();
	}
	
	@JoinColumns({
		@JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA", insertable = false, updatable = false),
		@JoinColumn(name = "ID_NF", referencedColumnName = "ID_NF", insertable = false, updatable = false)})
    @ManyToOne
	private NotaFiscalEletronica  notaFiscalEletronica;

	
	public NotaFiscalEletronica getNotaFiscalEletronica() {
		return notaFiscalEletronica;
	}
	public void setNotaFiscalEletronica(NotaFiscalEletronica notaFiscalEletronica) {
		this.notaFiscalEletronica = notaFiscalEletronica;
	}

	public void setItemNFEPK(ItemNFEPK itemNFEPK) {
		this.itemNFEPK = itemNFEPK;
	}

	public ItemNFEPK getItemNFEPK() {
		return itemNFEPK;
	}
	
	
}
