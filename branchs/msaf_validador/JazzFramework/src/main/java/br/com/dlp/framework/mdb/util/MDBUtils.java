package br.com.dlp.framework.mdb.util;

import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.mdb.daofactory.MDBDAOFactory;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.transaction.TransactionException;

public class MDBUtils {

	public static IDAO getDAO(IServiceLocator serviceLocator)
			throws DAOFactoryException, TransactionException {
		IDAO idao = getDAO(getIdDAO(), serviceLocator);
		return idao;
	}

	public static IDAO getDAO(String idDAO, IServiceLocator serviceLocator)
			throws TransactionException, DAOFactoryException {
		IDAOFactory factory;
		factory = getDAOFactory(serviceLocator);
		IDAO idao = factory.getDAO(idDAO);

		return idao;
	}

	public static String getIdDAO() {
		return br.com.dlp.framework.mdb.util.MDBConstants.MODULO_MDB;
	}

	public static IDAOFactory getDAOFactory(IServiceLocator serviceLocator)
			throws DAOFactoryException {
		return MDBDAOFactory.getInstance(serviceLocator);
	}

}
