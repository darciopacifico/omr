package br.com.dlp.framework.caching;

import br.com.dlp.framework.pk.IPK;

/**
 * Abstracao da chave para o vo MethodCacheVO
 */
public class InstanceCachePK extends AbstractCachePK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long codigo;

	public boolean comparePKValues(IPK pk) {
		InstanceCachePK instanceCachePK = (InstanceCachePK) pk;

		if (codigo != null) {
			return this.codigo.equals(instanceCachePK.codigo);
		} else {
			return false;
		}

	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
}
