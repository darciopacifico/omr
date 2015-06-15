package br.com.dlp.framework.servicelocator;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceLocatorFactory {
	private static ServiceLocatorFactory instance = null;

	private static Map chachedServiceLocators = new HashMap();

	private ServiceLocatorFactory() {
	}

	public static ServiceLocatorFactory getInstance() {
		if (instance == null) {
			instance = new ServiceLocatorFactory();
		}
		return instance;
	}

	public IServiceLocator getServiceLocator(String className)
			throws ServiceLocatorException {
		Class class1;
		try {
			class1 = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ServiceLocatorException(
					"Classe implementação de IServiceLocator não encontrada", e);
		}
		return getServiceLocator(class1);
	}

	/**
	 * Faz Cache das implementações de IServiceLocator de acordo com o nome da
	 * classe de implementação
	 */
	public IServiceLocator getServiceLocator(Class class1)
			throws ServiceLocatorException {
		String serviceLocatorClassName = class1.getName();
		IServiceLocator serviceLocator = (IServiceLocator) chachedServiceLocators
				.get(serviceLocatorClassName);

		if (serviceLocator == null) {
			serviceLocator = createServiceLocator(class1);
			chachedServiceLocators.put(serviceLocatorClassName, serviceLocator);
		} else {

			Log logger = LogFactory.getLog(ServiceLocatorFactory.class);
			if (logger.isDebugEnabled()) {
				logger.debug("Ja existe uma instancia para a implementacao "
						+ class1 + " de IServiceLocator ");
			}
		}

		return serviceLocator;
	}

	/**
	 * Executa o trabalho de instanciar o IServiceLocator
	 */
	private IServiceLocator createServiceLocator(Class class1)
			throws ServiceLocatorException {
		Log logger = LogFactory.getLog(ServiceLocatorFactory.class);
		if (logger.isDebugEnabled()) {
			logger.debug("Criando instancia da implementação " + class1
					+ " de IServiceLocator ");
		}

		IServiceLocator serviceLocator = null;
		try {

			Constructor constructor = class1.getConstructor(new Class[] {});
			Object object = constructor.newInstance(new Object[] {});
			serviceLocator = (IServiceLocator) object;

		} catch (Exception e) {
			/* substitui o ninho de catchs por um catch de Exception */
			throw new ServiceLocatorException(
					"NÃO FOI POSSÍVEL INSTANCIAR O SERVICE LOCATOR", e);
		}

		return serviceLocator;
	}

}
