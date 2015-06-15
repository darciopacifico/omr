package br.com.dlp.framework.mdb.util.servicelocator;

import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.mdb.daofactory.MDBDAOFactory;
import br.com.dlp.framework.servicelocator.AbstractJ2EEServiceLocatorImpl;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;

public class MDBServiceLocator extends AbstractJ2EEServiceLocatorImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2782939128451810538L;

	public static final String PROPERTIE_MDB_DS = "mdbDataSource";

	public MDBServiceLocator() throws ServiceLocatorException,
			DAOFactoryException {
		super();
		IDAOFactory daoFactory = MDBDAOFactory.getInstance(this);
		setDAOFactory(daoFactory);
	}

}
