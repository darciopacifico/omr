package br.com.dlp.framework.caching;

import java.lang.reflect.Method;

import br.com.dlp.framework.pk.IPK;

/**
 * Abstracao da chave para o vo MethodCacheVO
 */
public class MethodCachePK extends AbstractCachePK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String methodName;

	public MethodCachePK() {

	}

	public MethodCachePK(Method method) {
		setMethodName(method.getName());
	}

	public boolean comparePKValues(IPK pk) {
		MethodCachePK methodCachePK = (MethodCachePK) pk;

		if (methodName != null) {
			return this.methodName.equals(methodCachePK.methodName);
		} else {
			return false;
		}
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
