package br.com.dlp.framework.mdb.daofactory;

import br.com.dlp.framework.dao.AbstractDAOFactory;
import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;

public class MDBDAOFactory extends AbstractDAOFactory {

	private static MDBDAOFactory sistemaDAOFactory;

	/**
	 * @throws DAOFactoryException
	 * @throws PropertiesFactoryException
	 */
	protected MDBDAOFactory(IServiceLocator serviceLocator)
			throws DAOFactoryException, PropertiesFactoryException {
		super(serviceLocator);
	}

	public static MDBDAOFactory getInstance(IServiceLocator serviceLocator)
			throws DAOFactoryException {
		if (sistemaDAOFactory == null) {
			try {
				sistemaDAOFactory = new MDBDAOFactory(serviceLocator);
			} catch (PropertiesFactoryException e) {
				throw new DAOFactoryException(
						"Nao foi possivel criar uma instancia de MDBDAOFactory!",
						e);
			}
		}

		return sistemaDAOFactory;
	}

}
