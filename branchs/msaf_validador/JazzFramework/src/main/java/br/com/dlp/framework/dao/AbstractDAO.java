/*
 * Created on 27/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.dao;

import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementacao base do framework para IDAO
 */
public abstract class AbstractDAO implements IDAO {
	private IServiceLocator serviceLocator;

	/**
	 * Testa se o registro existe
	 */
	public boolean exists(IPK chave) throws DAOException, BaseTechnicalError {
		IBaseVO baseVO = findByPrimaryKey(chave);
		return baseVO != null;
	}

	/**
	 * Retorna a implementação de IServiceLocator do projeto
	 */
	protected IServiceLocator getServiceLocator()
			throws ServiceLocatorException {
		return this.serviceLocator;
	}

	public void setServiceLocator(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
}
