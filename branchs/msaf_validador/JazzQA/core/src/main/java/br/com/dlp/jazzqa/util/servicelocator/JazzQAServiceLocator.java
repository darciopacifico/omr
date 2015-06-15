package br.com.dlp.jazzqa.util.servicelocator;

import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.servicelocator.AbstractJ2EEServiceLocatorImpl;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.jazzqa.daofactory.JazzQADAOFactory;

public class JazzQAServiceLocator extends AbstractJ2EEServiceLocatorImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2823252978020278662L;

	public static final String PROPERTIE_JAZZQA_DS = "jazzqaDataSource";

	public JazzQAServiceLocator() throws ServiceLocatorException,
			DAOFactoryException {
		super();
		IDAOFactory daoFactory = JazzQADAOFactory.getInstance(this);
		setDAOFactory(daoFactory);
	}

}