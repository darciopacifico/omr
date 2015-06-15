package br.com.dlp.framework.caching;

import java.util.Collection;

/**
 * Abstrai o caching de uma chamada a um metodo com um determinado conjunto de
 * parametros
 */
public class MethodInvokeCacheVO extends AbstractCacheVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object cacheResult;

	private Long maximumSize;

	public MethodInvokeCacheVO(MethodInvokeCachePK methodInvokeCachePK) {
		super(methodInvokeCachePK);
	}

	public MethodInvokeCacheVO() {
		super(new MethodInvokeCachePK());
	}

	public Long getCurrentSize() {
		long returnValue = 0;

		if (cacheResult != null && (cacheResult instanceof Collection)) {
			returnValue = ((Collection) cacheResult).size();
		} else if (cacheResult != null) {
			returnValue = 1;
		}

		return new Long(returnValue);
	}

	public void fullClearCache() {
		cacheResult = null;
	}

	public Long getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(Long maximumSize) {
		this.maximumSize = maximumSize;
	}

	public void setCacheElements(Collection cacheElements) {

	}

	public void toLimitCache() {
		super.toLimitCache();
	}

	protected Class getChildClass() throws CachingException {
		throw new CachingException("Esta classe nao pode ter elementos filho!");
	}

	public Object getCacheResult() {
		return cacheResult;
	}

	public void setCacheResult(Object cacheResult) {
		this.cacheResult = cacheResult;
	}
}
