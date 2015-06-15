package br.com.mastersaf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import br.com.mastersaf.util.Bean;
import br.com.mastersaf.util.DateUtil;

@Entity
@Table(name = "NFE_ITEM_DI")
public class DiItemNFE implements Bean{

	private static final long serialVersionUID = 2185329266775306139L;

	@EmbeddedId
	private DiItemNFEPK diItemNFEPK;
	
	@Column(name="DT_REGISTRO" )
	private Date dtRegistro;  
	
	@Column(name="DS_LOCAL" )
	private String  local;
	
	@Column(name="DS_UF" )
	private String uf;
	
	@Column(name="DT_SAIDA" )
	private Date dtSaida; 
	
	@Column(name="CD_EXPORTADOR" )
	private String exportador;

	@Transient
	@SuppressWarnings("unused")
	private String saida;
	
	@Transient
	@SuppressWarnings("unused")
	private String registro;
	
	public DiItemNFE(){
		this.diItemNFEPK = new DiItemNFEPK();
	}

	public String getRegistro() {
		return DateUtil.formatStruts(dtRegistro);
	}

	public void setRegistro(String registro) {
		this.registro = registro;
		dtRegistro =DateUtil.formatStruts(registro);
	}
	public String getSaida() {
		return DateUtil.formatStruts(dtSaida);
	}

	public void setSaida(String saida) {
		this.saida = saida;
		dtSaida =DateUtil.formatStruts(saida);
	}

	public Date getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(Date dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Date getDtSaida() {
		return dtSaida;
	}

	public void setDtSaida(Date dtSaida) {
		this.dtSaida = dtSaida;
	}

	public String getExportador() {
		return exportador;
	}

	public void setExportador(String exportador) {
		this.exportador = exportador;
	}

	public void setDiItemNFEPK(DiItemNFEPK diItemNFEPK) {
		this.diItemNFEPK = diItemNFEPK;
	}

	public DiItemNFEPK getDiItemNFEPK() {
		return diItemNFEPK;
	}

}
