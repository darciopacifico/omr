package br.com.dlp.jazzqa.daofactory;

import br.com.dlp.framework.dao.AbstractProxiedDAOFactory;
import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;

public class JazzQADAOFactory extends AbstractProxiedDAOFactory {

	private static JazzQADAOFactory sistemaDAOFactory;

	/**
	 * @throws DAOFactoryException
	 * @throws PropertiesFactoryException
	 */
	protected JazzQADAOFactory(IServiceLocator serviceLocator)
			throws DAOFactoryException, PropertiesFactoryException {
		super(serviceLocator);
	}

	public static JazzQADAOFactory getInstance(IServiceLocator serviceLocator)
			throws DAOFactoryException {
		if (sistemaDAOFactory == null) {
			try {
				sistemaDAOFactory = new JazzQADAOFactory(serviceLocator);
			} catch (PropertiesFactoryException e) {
				throw new DAOFactoryException(
						"Nao foi possivel criar uma instancia de JazzQADAOFactory!",
						e);
			}
		}

		return sistemaDAOFactory;
	}

}