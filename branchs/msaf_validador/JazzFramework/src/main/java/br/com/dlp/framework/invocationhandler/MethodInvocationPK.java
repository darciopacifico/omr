package br.com.dlp.framework.invocationhandler;

import java.lang.reflect.Method;

import br.com.dlp.framework.pk.IPK;

/**
 * Abstrai a unicidade de uma chama á um metodo de um objeto qualquer, para que
 * seja possivel manipular e interceptar a chamada
 */
public class MethodInvocationPK implements IPK {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3445211898154522714L;

	private Object object;

	private Method method;

	private Object[] objects;

	public MethodInvocationPK(Object object, Method method, Object[] objects) {
		this.object = object;
		this.method = method;
		this.objects = objects;
	}

	public boolean compare(IPK pk) {
		return false;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

}
