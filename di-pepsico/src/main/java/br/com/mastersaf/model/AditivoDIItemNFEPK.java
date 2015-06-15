package br.com.mastersaf.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author lzanardi
 */
@Embeddable
public class AditivoDIItemNFEPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_EMPRESA", nullable = false)
	private Long idEmpresa;
	@Column(name = "NR_DOCUMENTO", nullable = false)
	private String nrDocumento;
	@Column(name = "ID_ITEM", nullable = false)
	private Long idItem;
	@Column(name = "ID_ADICAO", nullable = false)
	private Long idAdicao;
	@Column(name = "ID_NF", nullable = false)
	private Long idNf;

	public AditivoDIItemNFEPK() {
	}

	public AditivoDIItemNFEPK(Long idEmpresa, String nrDocumento, Long idItem,
			Long idAdicao, Long idNf) {
		this.idEmpresa = idEmpresa;
		this.nrDocumento = nrDocumento;
		this.idItem = idItem;
		this.idAdicao = idAdicao;
		this.idNf = idNf;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Long getIdItem() {
		return idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	public Long getIdAdicao() {
		return idAdicao;
	}

	public void setIdAdicao(Long idAdicao) {
		this.idAdicao = idAdicao;
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
		hash += (nrDocumento != null ? nrDocumento.hashCode() : 0);
		hash += (idItem != null ? idItem.hashCode() : 0);
		hash += (Long) idAdicao;
		hash += (idNf != null ? idNf.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof AditivoDIItemNFEPK)) {
			return false;
		}
		AditivoDIItemNFEPK other = (AditivoDIItemNFEPK) object;
		if (this.idEmpresa != other.idEmpresa) {
			return false;
		}
		if ((this.nrDocumento == null && other.nrDocumento != null)
				|| (this.nrDocumento != null && !this.nrDocumento
						.equals(other.nrDocumento))) {
			return false;
		}
		if ((this.idItem == null && other.idItem != null)
				|| (this.idItem != null && !this.idItem.equals(other.idItem))) {
			return false;
		}
		if (this.idAdicao != other.idAdicao) {
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
		return "br.com.mastersaf.model.AditivoDIItemNFEPK[idEmpresa="
				+ idEmpresa + ", nrDocumento=" + nrDocumento + ", idItem="
				+ idItem + ", idAdicao=" + idAdicao + ", idNf=" + idNf + "]";
	}

}
