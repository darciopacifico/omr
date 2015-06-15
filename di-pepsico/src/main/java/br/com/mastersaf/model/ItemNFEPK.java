package br.com.mastersaf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ItemNFEPK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID_EMPRESA", nullable = false)
	private Long idEmpresa;
	@Column(name = "ID_ITEM", nullable = false)
	private Long idItem;
	@Column(name = "ID_NF", nullable = false)
	private Long idNf;
	
	public ItemNFEPK(){
	}
	
	public ItemNFEPK(Long idEmpresa, Long idItem, Long idNf){
		this.idEmpresa = idEmpresa;
		this.idItem = idItem;
		this.idNf = idNf;
	}
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	public Long getIdItem() {
		return idItem;
	}
	
	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}
	
	public Long getIdNf() {
		return idNf;
	}
	
	public void setIdNf(Long idNf) {
		this.idNf = idNf;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += idEmpresa;
		hash += idItem != null ? idItem.hashCode() : 0;
		hash += idNf != null ? idNf.hashCode() : 0;
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ItemNFEPK)) {
			return false;
		}
		ItemNFEPK other = (ItemNFEPK) object;
		if (idEmpresa != other.idEmpresa) {
			return false;
		}
		
		if (idItem == null && other.idItem != null
				|| idItem != null && !idItem.equals(other.idItem)) {
			return false;
		}
		if (idNf == null && other.idNf != null
				|| idNf != null && !idNf.equals(other.idNf)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "br.com.mastersaf.model.ItemNFEPK[idEmpresa="
		+ idEmpresa + ", idItem="
		+ idItem + ", idNf=" + idNf + "]";
	}
	
}
