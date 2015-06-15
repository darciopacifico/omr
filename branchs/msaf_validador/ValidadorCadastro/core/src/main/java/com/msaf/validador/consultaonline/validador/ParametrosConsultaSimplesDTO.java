/**
 * 
 */
package com.msaf.validador.consultaonline.validador;

import java.io.Serializable;

/**
 * @author dlopes
 *
 */
public class ParametrosConsultaSimplesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1805638739020785743L;
	public ParametrosConsultaSimplesDTO(){}
	
	private String estado;
	private String documento;
	private String tipoDocumento;
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}
	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public Integer getServico() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getParametros() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getDllPath() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getEvServer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
