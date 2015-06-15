/*
 * Created on 07/07/2005
 */
package br.com.dlp.framework.pk;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Abstracao de Primary key
 */
public abstract class AbstractPK implements IPK {
	
	private static final long serialVersionUID = -7189776130810120470L;
	
	/**
	 * Default constructor
	 */
	public AbstractPK() {
		// intencionalmente em branco
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof AbstractPK) {// instanceof implicitamente checa null
			return hashCode() == obj.hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
}
