package br.com.dlp.framework.invocationhandler;

import java.lang.reflect.Method;

/**
 * Implementacao abstrata basica de IBaseInvocationHandler
 */
public abstract class AbstractInvocationHandler implements
		IBaseInvocationHandler {
	private Object proxiedObject;

	public AbstractInvocationHandler() {
	};

	public Object invoke(Object object, Method method, Object[] objects)
			throws Throwable {
		Object returnObject;
		returnObject = method.invoke(proxiedObject, objects);
		return returnObject;
	}

	public Object getProxiedObject() {
		return proxiedObject;
	}

	public void setProxiedObject(Object proxiedObject) {
		this.proxiedObject = proxiedObject;
	}
}
