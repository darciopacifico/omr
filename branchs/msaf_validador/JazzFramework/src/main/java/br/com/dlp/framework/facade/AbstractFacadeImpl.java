package br.com.dlp.framework.facade;

import java.rmi.RemoteException;

import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorFactory;

public abstract class AbstractFacadeImpl implements IFacade {
	/**
	 * Recupera o Facade indicado no argumento moduleId
	 */
	protected ICadastroFacade getFacade(String moduleId)
			throws BaseTechnicalError {
		ICadastroFacade facade;
		try {
			IServiceLocator serviceLocator = getServiceLocator();
			facade = (ICadastroFacade) serviceLocator.getFacade(moduleId);
		} catch (BaseTechnicalError e) {
			throw new BaseTechnicalError("Erro ao tentar criar facade !", e);
		}
		return facade;
	}

	/**
	 * Retorna a implementacao de IServiceLocator
	 * 
	 * @throws BaseTechnicalError
	 */
	protected IServiceLocator getServiceLocator(boolean remoteFacades)
			throws BaseTechnicalError {
		ServiceLocatorFactory serviceLocatorFactory = ServiceLocatorFactory
				.getInstance();
		Class serviceLocatorClass = getServiceLocatorClass();
		IServiceLocator serviceLocator = serviceLocatorFactory
				.getServiceLocator(serviceLocatorClass);

		serviceLocator.setRemoteFacades(remoteFacades);

		return serviceLocator;
	}

	protected IServiceLocator getServiceLocator() throws BaseTechnicalError {
		/*
		 * Provavelmente eu estou no app Server (Sou um facade OK?) então, por
		 * padrão, os outros componentes que eu acessar serão locais
		 */

		return getServiceLocator(false);
	}

	/**
	 * Retorna a classe do ServiceLocator que será utilizado pelo Facade
	 */
	protected abstract Class getServiceLocatorClass() throws BaseTechnicalError;

	/**
	 * Implementar este método de modo a desalocar os recursos utilizados nesta
	 * fachada
	 */
	public void finishFacade() throws BaseTechnicalError, RemoteException {

	}
}
