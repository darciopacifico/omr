/*
 * Created on 29/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.caching;

/**
 * Abstrai o controle de caching de um metodo específico
 */
public class MethodCacheVO extends AbstractCacheVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long maximumSize;

	public MethodCacheVO() {
		super(new MethodCachePK());
	}

	public Long getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(Long maximumSize) {
		this.maximumSize = maximumSize;
	}

	protected Class getChildClass() throws CachingException {
		return MethodInvokeCacheVO.class;
	}
}
