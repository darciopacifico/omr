package br.com.dlp.framework.invocationhandler;

import java.lang.reflect.InvocationHandler;

/**
 * Contrato basico para InvocationHandler
 */
public interface IBaseInvocationHandler extends InvocationHandler {
	public Object getProxiedObject() throws InvocationHandlerException;

	public void setProxiedObject(Object proxiedObject)
			throws InvocationHandlerException;
}
