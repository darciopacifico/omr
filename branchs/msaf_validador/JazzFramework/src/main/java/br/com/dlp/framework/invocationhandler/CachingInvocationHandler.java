/*
 * Created on 26/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.invocationhandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CachingInvocationHandler implements InvocationHandler {
	private Object proxiedObject;

	Map cache = new HashMap(2);

	public CachingInvocationHandler(Object proxiedObject) {
		this.proxiedObject = proxiedObject;
	}

	public Object invoke(Object object, Method method, Object[] objects)
			throws Throwable {
		Object returnObject;

		String methodName = method.getName();

		if (methodName.equals("findStatusHistoricoVO")) {
			if (cache.containsKey("findStatusHistoricoVO")) {
				returnObject = cache.get("findStatusHistoricoVO");
			} else {
				returnObject = method.invoke(proxiedObject, objects);
				cache.put("findStatusHistoricoVO", returnObject);
			}
		} else {
			returnObject = method.invoke(proxiedObject, objects);
		}

		return returnObject;

	}

}
