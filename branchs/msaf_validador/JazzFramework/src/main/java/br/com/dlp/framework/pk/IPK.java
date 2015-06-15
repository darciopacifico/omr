package br.com.dlp.framework.pk;

import java.io.Serializable;

public abstract interface IPK extends Serializable {
	public abstract boolean compare(IPK pk);

}
