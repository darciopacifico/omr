package br.com.dlp.framework.dao;

import java.io.Serializable;

/**
 * DTO para transportar escolhas de ordenacao de pesquisas
 * 
 * @author t_dpacifico
 * 
 */
public class QueryOrder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4490514737914281036L;
	
	private boolean asc = true;
	
	private String campo;
	
	/**
	 * Apenas para cumprir exigências para geracao de webServices
	 */
	public QueryOrder() {
	}
	
	public QueryOrder(final String campo) {
		this.campo = campo;
	}
	
	public QueryOrder(final String campo, final boolean asc) {
		this.campo = campo;
		this.asc = asc;
	}
	
	public String getCampo() {
		return campo;
	}
	
	public boolean isAsc() {
		return asc;
	}
	
	public void setAsc(final boolean asc) {
		this.asc = asc;
	}
	
	public void setCampo(final String campo) {
		this.campo = campo;
	}
}
