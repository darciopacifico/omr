package br.com.dlp.framework.unittest.pk;

import org.hibernate.dialect.Oracle9Dialect;

import br.com.dlp.framework.pk.AbstractAutoComparePK;

public class PedidoPK extends AbstractAutoComparePK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4435320201605750929L;

	private Long id;

	private String zuba;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZuba() {
		return zuba;
	}

	public void setZuba(String zuba) {
		this.zuba = zuba;
	}

}
