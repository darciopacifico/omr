package br.com.dlp.framework.caching;

/**
 * Abstrai o controle de caching de uma classe específica
 * 
 */
public class ClassCacheVO extends AbstractCacheVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClassCacheVO() {
		super(new ClassCachePK());
	}

	public String getClassName() {
		ClassCachePK classCachePK = (ClassCachePK) getIPK();
		return classCachePK.getClassName();
	}

	public void setClassName(String className) {
		ClassCachePK classCachePK = (ClassCachePK) getIPK();
		classCachePK.setClassName(className);
	}

	public void setClassName(Class clazz) {
		String className = clazz.getName();
		setClassName(className);
	}

	protected Class getChildClass() throws CachingException {
		return MethodCacheVO.class;
	}

}
