package br.com.dlp.framework.dao;

import java.io.Serializable;

public class QueryOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4490514737914281036L;

	private boolean asc = true;

	private String campo;

	public QueryOrder(String campo) {
		this.campo = campo;
	}

	public QueryOrder(String campo, boolean asc) {
		this.asc = asc;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getCampo() {
		return campo;
	}
}
