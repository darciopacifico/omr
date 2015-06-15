package br.com.dlp.framework.caching;

import br.com.dlp.framework.pk.IPK;

/**
 * Abstracao da chave para o vo MethodCacheVO
 */
public class ClassCachePK extends AbstractCachePK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className;

	public ClassCachePK() {
	}

	public ClassCachePK(String className) {
		setClassName(className);
	}

	public ClassCachePK(Class clazz) throws CachingException {

		if (clazz == null) {
			throw new CachingException(
					"O objeto 'Class' informado para a chave ClassCachePK nao pode ser nulo!");
		}

		setClassName(clazz.getName());
	}

	public boolean comparePKValues(IPK pk) {
		ClassCachePK classCachePK = (ClassCachePK) pk;

		if (className != null) {
			return this.className.equals(classCachePK.className);
		} else {
			return false;
		}
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
