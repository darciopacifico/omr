/*
 * Created on 29/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.caching;

import br.com.dlp.framework.pk.AbstractPK;
import br.com.dlp.framework.pk.IPK;

/**
 * Abstracao de PK para vo de cache
 */
public abstract class AbstractCachePK extends AbstractPK {

	public AbstractCachePK() {

	}

	public boolean compare(IPK pk) {

		if (pk == null) {
			return false;

		} else if (pk == this) {
			return true;

		} else {
			if (this.getClass().isAssignableFrom(pk.getClass())) {
				return comparePKValues(pk);
			} else {
				return false;
			}
		}
	}

	public abstract boolean comparePKValues(IPK pk);

}
