package br.com.mastersaf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NotaFiscalEletronicaPK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID_EMPRESA", nullable = false)
	private Long idEmpresa;
	@Column(name = "ID_NF", nullable = false)
	private Long idNf;
	
	public NotaFiscalEletronicaPK(){
	}
	
	public NotaFiscalEletronicaPK(Long idEmpresa, Long idNf){
		this.idEmpresa = idEmpresa;
		this.idNf = idNf;
	}
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
		hash += (Long) idEmpresa;
		hash += (idNf != null ? idNf.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof NotaFiscalEletronicaPK)) {
			return false;
		}
		NotaFiscalEletronicaPK other = (NotaFiscalEletronicaPK) object;
		if (this.idEmpresa != other.idEmpresa) {
			return false;
		}
		if ((this.idNf == null && other.idNf != null)
				|| (this.idNf != null && !this.idNf.equals(other.idNf))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.com.mastersaf.model.NotaFiscalEletronicaPK[idEmpresa="
				+ idEmpresa + ", idNf=" + idNf + "]";
	}
}
