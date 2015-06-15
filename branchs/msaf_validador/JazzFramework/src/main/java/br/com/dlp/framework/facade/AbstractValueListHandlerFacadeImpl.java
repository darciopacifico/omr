package br.com.dlp.framework.facade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorFactory;

public abstract class AbstractValueListHandlerFacadeImpl implements
		IValueListHandlerFacade {

	protected Log logger = LogFactory
			.getLog(AbstractValueListHandlerFacadeImpl.class);

	private List fullResult = new ArrayList();

	private int pageSize = 6;

	private int pageIndex = 0;

	private int pages = 0;

	/**
	 * Retornar o id do módulo para encontrar a implementação de DAO
	 */
	public abstract String getModuleId();

	/**
	 * Retornar o id do módulo VLH
	 */
	public abstract String getModuleVLHId();

	/**
	 * Implementar retornando o DAOFactory do projeto
	 * 
	 * @throws BaseTechnicalError
	 */
	protected IDAOFactory getDAOFactory() throws BaseTechnicalError {
		IServiceLocator serviceLocator = getServiceLocator();
		IDAOFactory daoFactory = serviceLocator.getDAOFactory();
		return daoFactory;
	}

	/**
	 * Retorna a implementacao de IServiceLocator
	 * 
	 * @throws BaseTechnicalError
	 */
	protected IServiceLocator getServiceLocator() throws BaseTechnicalError {
		ServiceLocatorFactory serviceLocatorFactory = ServiceLocatorFactory
				.getInstance();
		Class serviceLocatorClass = getServiceLocatorClass();
		IServiceLocator serviceLocator = serviceLocatorFactory
				.getServiceLocator(serviceLocatorClass);
		return serviceLocator;
	}

	protected abstract Class getServiceLocatorClass() throws BaseTechnicalError;

	/**
	 * Retorna uma implementação de IDAO de acordo com getModuleId()
	 * 
	 * @throws BaseTechnicalError
	 */
	protected IDAO getDAO() throws BaseTechnicalError {

		String moduleId = getModuleId();
		if (logger.isDebugEnabled()) {
			logger
					.debug("Modulo do sistema para construcao da implementacao de IDAO:"
							+ moduleId);
		}

		IDAOFactory factory = getDAOFactory();
		IDAO dao = factory.getDAO(moduleId);
		return dao;
	}

	/**
	 * Recuperar o VLHpageSize padrao do modulo a partir do ServiceLocator
	 */
	protected int getVLHPageSize() throws ValueListHandlerException {
		String moduleVLHId = getModuleVLHId();
		IServiceLocator serviceLocator;
		int vlhPageSize;
		try {
			serviceLocator = getServiceLocator();
			vlhPageSize = serviceLocator.getVlhPageSize(moduleVLHId);
		} catch (BaseTechnicalError e) {
			throw new ValueListHandlerException(
					"Erro ao tentar descobrir VLHpageSize do modulo '"
							+ moduleVLHId + "'", e);
		}

		return vlhPageSize;
	}

	/**
	 * Retorna uma implementação de IDAO de acordo com getModuleId()
	 * 
	 * @throws BaseTechnicalError
	 */
	protected IDAO getDAO(String moduleId) throws BaseTechnicalError {

		if (logger.isDebugEnabled()) {
			logger
					.debug("Modulo do sistema para construcao da implementacao de IDAO:"
							+ moduleId);
		}

		IDAOFactory factory = getDAOFactory();
		IDAO dao = factory.getDAO(moduleId);
		return dao;
	}

	/**
	 * Este método deve invocar um determinado DAO e atribuir o valor retornado
	 * no método setFullResult(List fullResult)
	 */
	public void executeFindAll() throws ValueListHandlerException,
			RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("pesquisando todos os resultados");
		}

		IDAO dao;
		try {

			dao = getDAO();
			setFullResult(dao.findAll());

		} catch (DAOFactoryException e) {
			throw new ValueListHandlerException(
					"Nao foi possivel construir um DAO para :" + getModuleId(),
					e);

		} catch (DAOException e) {
			throw new ValueListHandlerException(e);

		} catch (BaseTechnicalError e) {
			throw new ValueListHandlerException("Erro tecnico não esperado", e);

		}
	}

	public void executeFindAll(QueryOrder[] queryOrders)
			throws ValueListHandlerException, RemoteException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("pesquisando todos os resultados utilizando ordenacao");
		}

		IDAO dao;
		try {

			dao = getDAO();
			setFullResult(dao.findAll(queryOrders));

		} catch (DAOFactoryException e) {
			throw new ValueListHandlerException(
					"Não foi possível construir um DAO para :" + getModuleId(),
					e);

		} catch (DAOException e) {
			throw new ValueListHandlerException(e);

		} catch (BaseTechnicalError e) {
			throw new ValueListHandlerException("Erro técnico não esperado", e);

		}
	}

	public List getPage() throws ValueListHandlerException, RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Retornando a pagina atual");
		}

		int de = (getPageIndex() * getPageSize());
		int ate = (de + getPageSize());

		int fullResultSize = getFullResult().size();
		if (ate > fullResultSize) {
			ate = fullResultSize;
		}

		/**
		 * Não posso retornar a sublista "subList(de,ate)" diretamente, pois
		 * esta não é serializavel
		 */
		List returnValue = new ArrayList(getFullResult().subList(de, ate));

		return returnValue;
	}

	public List getPage(int index) throws ValueListHandlerException,
			RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando a pagina atual para o indice:" + index);
		}

		setPageIndex(index);
		return getPage();
	}

	public int getPages() throws ValueListHandlerException, RemoteException {
		return pages;
	}

	public int getPageSize() throws ValueListHandlerException {
		if (this.pageSize == 0) {
			this.pageSize = getVLHPageSize();
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) throws ValueListHandlerException,
			RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando o tamanho de pagina para:" + pageSize);
		}

		if (pageSize < 1) {
			throw new ValueListHandlerException(
					"NÃO É POSSÍVEL ATRIBUIR UM TAMANHO DE PÁGINA MENOR DO QUE UM");
		}
		this.pageSize = pageSize;
	}

	public List getFirstPage() throws ValueListHandlerException,
			RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando a pagina atual para a primeira pagina");
		}

		setPageIndex(0);
		return getPage();
	}

	public List getLastPage() throws ValueListHandlerException, RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando a pagina atual para a ultima pagina");
		}

		setPageIndex(pages - 1);
		return getPage();
	}

	public List getNextPage() throws ValueListHandlerException, RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando a pagina atual para a proxima pagina");
		}

		setPageIndex(getPageIndex() + 1);
		return getPage();
	}

	public List getPreviousPage() throws ValueListHandlerException,
			RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando a pagina atual para a pagina anterior");
		}

		setPageIndex(getPageIndex() - 1);
		return getPage();
	}

	public int getPageIndex() throws RemoteException {
		return pageIndex;
	}

	private void setPageIndex(int paginaAtual) {
		if (logger.isDebugEnabled()) {
			logger.debug("Alterando o indice de pagina para " + paginaAtual
					+ " ...");
		}

		/* IFS PARA MANTER A PÁGINA ATUAL DENTRO DO LIMITE */
		if (paginaAtual < 0) {
			paginaAtual = 0;
		} else if (paginaAtual > (pages - 1)) {
			paginaAtual = pages - 1;

			if (paginaAtual < 0) {
				paginaAtual = 0;
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("... o indice permitido para alteracao foi "
					+ paginaAtual);
		}
		this.pageIndex = paginaAtual;
	}

	protected void setFullResult(List fullResult) throws RemoteException,
			ValueListHandlerException {
		int fullSize = fullResult.size();

		if (logger.isDebugEnabled()) {
			logger.debug("Tamanho total da pesquisa = " + fullSize
					+ ", tamanho de pagina " + getPageSize());
		}

		int paginas = (fullSize / getPageSize());
		if (fullSize % getPageSize() > 0) {
			paginas++;
		}
		/* atribui a quantidade de páginas atual */
		this.pages = paginas;

		/* reavalia a página atual */
		setPageIndex(getPageIndex());

		/* atribui o resultado total da query */
		this.fullResult = fullResult;
	}

	protected List getFullResult() {
		return fullResult;
	}

	/**
	 * Implementar este método de modo a desalocar os recursos utilizados nesta
	 * fachadaS
	 */
	public void finishFacade() throws BaseTechnicalError, RemoteException {
		setFullResult(new ArrayList(1));
	}
}