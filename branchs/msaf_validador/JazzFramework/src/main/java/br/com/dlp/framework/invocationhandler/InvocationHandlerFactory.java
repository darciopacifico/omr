package br.com.dlp.framework.invocationhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;

/**
 * Complementa as funcionalidades da implementacao de IDAOFactory AbstractProxiedDAOFactory
 */
public class InvocationHandlerFactory {
	protected Log logger = LogFactory.getLog(InvocationHandlerFactory.class);

	private static InvocationHandlerFactory invocationHandlerFactory;

	private InvocationHandlerFactory() {
	}

	/**
	 * Singleton
	 */
	public static InvocationHandlerFactory getInstance() {

		if (invocationHandlerFactory == null) {

			invocationHandlerFactory = new InvocationHandlerFactory();
		}

		return invocationHandlerFactory;
	}

	/**
	 * Constroi um implementacao de InvocationHandler com base em propriedades
	 */
	public IBaseInvocationHandler getInvocationHandler(Object proxiedObject, String invocationHandlerClassName) throws InvocationHandlerException {
		
		IBaseInvocationHandler baseInvocationHandler;
		
		if (logger.isDebugEnabled()) {
			logger.debug("criando um invocationHandler para " + invocationHandlerClassName);
		}
		
		try {
			baseInvocationHandler = (IBaseInvocationHandler) FrameworkAcheUtil.instanciaObjeto(invocationHandlerClassName, new Class[] {}, new Object[] {});

			baseInvocationHandler.setProxiedObject(proxiedObject);

		} catch (FrameworkAcheUtilError e) {
			throw new InvocationHandlerException("Erro ao tentar instanciar a implementacao de InvocationHandler para o invocationHandlerClassName = " + invocationHandlerClassName, e);
		}

		return baseInvocationHandler;
	}

	

	/**
	 * Constroi um implementacao de InvocationHandler com base em propriedades
	 */
	public IBaseInvocationHandler getInvocationHandler(Object proxiedObject, Class invocationHandlerClass) throws InvocationHandlerException {
		
		IBaseInvocationHandler baseInvocationHandler;
		
		if (logger.isDebugEnabled()) {
			logger.debug("criando um invocationHandler para " + invocationHandlerClass);
		}
		
		try {
			baseInvocationHandler = (IBaseInvocationHandler) FrameworkAcheUtil.instanciaObjeto(invocationHandlerClass, new Class[] {}, new Object[] {});

			baseInvocationHandler.setProxiedObject(proxiedObject);

		} catch (FrameworkAcheUtilError e) {
			throw new InvocationHandlerException("Erro ao tentar instanciar a implementacao de InvocationHandler para o invocationHandlerClassName = " + invocationHandlerClass, e);
		}

		return baseInvocationHandler;
	}
}
