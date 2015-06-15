package br.com.dlp.jazzqa.base;

import br.com.dlp.framework.pk.AbstractAutoComparePK;

public abstract class AbstractJazzQAPK extends AbstractAutoComparePK {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
