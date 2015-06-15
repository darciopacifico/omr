package br.com.dlp.framework.caching;

public class InstanceCacheVO extends AbstractCacheVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InstanceCacheVO() {
		super(new InstanceCachePK());
	}

	protected Class getChildClass() throws CachingException {
		return ClassCacheVO.class;
	}

}
