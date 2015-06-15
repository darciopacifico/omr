package br.com.dlp.framework.facade;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.exception.LockException;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.transaction.ITransaction;
import br.com.dlp.framework.transaction.Transacional;
import br.com.dlp.framework.transaction.TransactionException;
import br.com.dlp.framework.transaction.TransactionFactory;
import br.com.dlp.framework.transaction.TransactionFactoryException;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;
import br.com.dlp.framework.vo.ICadastroBaseVO;

public abstract class AbstractCadastroFacadeImpl extends AbstractFacadeImpl
		implements ICadastroFacade {
	private ITransaction transaction;

	protected Log logger = LogFactory.getLog(AbstractCadastroFacadeImpl.class);

	public abstract String getModuleId() throws BaseTechnicalError;

	/**
	 * Retorna a classe VO do respectivo módulo. A implementação padrão do
	 * método newVO() utiliza este método para criar um VO novo não persistido e
	 * sem chave primária. Espera-se que este método seja invocado no momento em
	 * que o usuário do sistema deseje criar um novo registro.
	 * 
	 * @return Classe do módulo
	 * @throws BaseBusinessException
	 *             Exceção de negócio ao tentar retornar a classe VO do módulo
	 * @throws BaseTechnicalError
	 *             Exceção ténica ao tentar retornar a classe VO do módulo
	 */
	protected abstract Class getVOClass() throws BaseBusinessException,
			BaseTechnicalError;

	/**
	 * Implementação padrão de estratégia de lock para telas de cadastro
	 */
	public ICadastroBaseVO lockObject(ICadastroBaseVO baseVO)
			throws BaseBusinessException, BaseTechnicalError, RemoteException {
		IServiceLocator serviceLocator = getServiceLocator();
		Session session = serviceLocator.getHibernateSession();

		try {
			session.lock(baseVO, LockMode.UPGRADE_NOWAIT);

		} catch (HibernateException e) {
			throw new LockException(
					"Erro ao tentar reservar registro para alteracao. "
							+ "Provavelmente outro usuário está alterando este registro!",
					e);

		}

		return baseVO;
	}

	/**
	 * Implementação padrão de newVO() - vide comentário do método getVOClass()
	 * 
	 * @return Novo objeto do tipo (Class) especificado no método getVOClass()
	 * @throws BaseBusinessException
	 *             Exceção de negócio ao tentar criar o novo objeto
	 * @throws BaseTechnicalError
	 *             Exceção técnica ao tentar criar o novo objeto
	 * @throws RemoteException
	 *             Exceção padrão do contrato RMI/EJB 2.x (uma m.... por sinal!)
	 */
	public ICadastroBaseVO newVO() throws BaseBusinessException,
			BaseTechnicalError, RemoteException {
		Class clazz = getVOClass();
		if (clazz == null) {
			throw new BaseTechnicalError(
					"Nao e possivel criar um novo vo! "
							+ "O metodo getVOClass() esta retornando um objeto 'Class' nulo!");
		}

		ICadastroBaseVO cadastroBaseVO;
		try {

			cadastroBaseVO = (ICadastroBaseVO) FrameworkAcheUtil
					.instanciaObjeto(clazz, new Class[] {}, new Object[] {});

		} catch (FrameworkAcheUtilError e) {
			throw new DAOException(
					"Não foi possivel criar uma instancia de VO a partir da classe informada:"
							+ clazz.getName(), e);
		}

		return cadastroBaseVO;
	}

	/**
	 * Adiciona um recurso transacional ao gerenciador de transações caso o
	 * gerenciador tenha sido criado
	 */
	protected void addToTransaction(Transacional transacional)
			throws TransactionException {
		if (transaction != null && transacional != null) {
			transaction.addTransacionalResource(transacional);
		}
	}

	/**
	 * Adiciona avalia um recurso e caso este implemente Transacional será
	 * adicionado à transação corrente caso esta exista
	 */
	protected void addToTransactionIfIsTransacional(Object object)
			throws TransactionException {
		if (object != null && object instanceof Transacional) {
			addToTransaction((Transacional) object);
		}
	}

	/**
	 * Retorna a implementação de IDAO específica do Módulo e Banco de Dados
	 * corrente
	 * 
	 * @throws BaseTechnicalError
	 */
	public IDAO getDAO() throws BaseTechnicalError {
		IDAO idao = getDAO(getModuleId());
		return idao;
	}

	/**
	 * Retorna a implementacao de DAOFactory escolhida.<br>
	 * Este método deve estar implementado na base da abstracao do contrado de
	 * Facade do projeto.<br>
	 * Ex: br.com.dlp.backlog.base.AbstractBacklogFacadeImpl
	 * 
	 * @throws BaseTechnicalError
	 * @throws RemoteException
	 * @throws BaseTechnicalError
	 */
	protected IDAOFactory getDAOFactory() throws BaseTechnicalError {
		IServiceLocator serviceLocator = getServiceLocator();
		IDAOFactory daoFactory = serviceLocator.getDAOFactory();
		return daoFactory;
	}

	/**
	 * Retorna a implementação de IDAO do Módulo especificado e Banco de Dados
	 * corrente
	 * 
	 * @throws BaseTechnicalError
	 * @throws PropertiesFactoryException
	 * @throws PropertiesFactoryException
	 */
	public IDAO getDAO(String idDAO) throws BaseTechnicalError {
		IDAOFactory factory;
		factory = getDAOFactory();
		IDAO idao = factory.getDAO(idDAO);

		addToTransactionIfIsTransacional(idao);

		return idao;
	}

	/**
	 * Cria um gerenciador de transação. É importante q caso se deseje trabalhar
	 * com transações o getNewTransaction() seja o primeiro método a ser
	 * invocado em seu método de negócio e a referência de ITransaction seja
	 * mantida e utilizada enquanto durar a transação, que na maioria dos casos
	 * é apenas o tempo de duração do método de negócio
	 * 
	 * @throws TransactionFactoryException
	 */
	public ITransaction getNewTransaction() throws TransactionFactoryException {
		transaction = null;

		TransactionFactory factory = TransactionFactory.getInstance();
		transaction = factory.getTransaction(this);

		return transaction;
	}

	public ICadastroBaseVO incluir(ICadastroBaseVO baseVO)
			throws BaseTechnicalError, BaseBusinessException {

		validacoesDeNegocioIncluirAlterar(baseVO);
		validacoesDeNegocioIncluir(baseVO);

		IDAO dao;
		try {

			dao = getDAO();
			baseVO = dao.inserir(baseVO);

		} catch (DAOFactoryException e) {
			throw new BaseTechnicalError(e);

		} catch (DAOException e) {
			throw new BaseBusinessException(e);

		}

		return baseVO;
	}

	public ICadastroBaseVO alterar(ICadastroBaseVO iBaseVO)
			throws BaseTechnicalError, BaseBusinessException {
		validacoesDeNegocioIncluirAlterar(iBaseVO);
		validacoesDeNegocioAlterar(iBaseVO);

		IDAO dao;
		try {

			dao = getDAO();
			iBaseVO = dao.atualizar(iBaseVO);

		} catch (DAOFactoryException e) {
			throw new BaseTechnicalError(e);
		} catch (DAOException e) {
			throw new BaseBusinessException(e);
		}
		return iBaseVO;
	}

	public void excluir(ICadastroBaseVO iBaseVO) throws BaseTechnicalError,
			BaseBusinessException {
		validacoesDeNegocioExcluir(iBaseVO);

		IDAO dao;
		try {

			dao = getDAO();
			dao.excluir(iBaseVO);

		} catch (DAOFactoryException e) {
			throw new BaseTechnicalError(e);
		} catch (DAOException e) {
			throw new BaseBusinessException(e);
		}
	}

	public ICadastroBaseVO findByPrimaryKey(IPK chave)
			throws BaseTechnicalError, BaseBusinessException {
		IDAO dao;
		try {

			dao = getDAO();
			ICadastroBaseVO iBaseVO = dao.findByPrimaryKey(chave);
			return iBaseVO;

		} catch (DAOFactoryException e) {
			throw new BaseTechnicalError(e);
		} catch (DAOException e) {
			throw new BaseBusinessException(e);
		}
	}

	public boolean exists(IPK pk) throws BaseBusinessException,
			BaseTechnicalError, RemoteException {
		IDAO dao;
		try {

			dao = getDAO();
			return dao.exists(pk);

		} catch (DAOFactoryException e) {
			throw new BaseTechnicalError(e);
		} catch (DAOException e) {
			throw new BaseBusinessException(e);
		}
	}

	public List findAll() throws BaseTechnicalError, BaseBusinessException {
		IDAO dao;
		try {

			dao = getDAO();
			List list = dao.findAll();
			return list;

		} catch (DAOFactoryException e) {
			throw new BaseTechnicalError(e);
		} catch (DAOException e) {
			throw new BaseBusinessException(e);
		}
	}

	/**
	 * Implemente aqui as validações de negócio referentes aos métodos de
	 * inclusão e alteração do seu módulo. As exceptions de validação de negócio
	 * devem ser acumuladas num BaseBusinessException e depois este deve ser
	 * lancado
	 */
	public abstract void validacoesDeNegocioIncluirAlterar(ICadastroBaseVO id)
			throws BaseBusinessException;

	/**
	 * Implemente aqui as validações de negócio do seu módulo. As exceptions de
	 * validação de negócio devem ser acumuladas num BaseBusinessException e
	 * depois este deve ser lancado
	 */
	public abstract void validacoesDeNegocioIncluir(ICadastroBaseVO baseVO)
			throws BaseBusinessException;

	/**
	 * Implemente aqui as validações de negócio do seu módulo. As exceptions de
	 * validação de negócio devem ser acumuladas num BaseBusinessException e
	 * depois este deve ser lancado
	 */
	public abstract void validacoesDeNegocioAlterar(ICadastroBaseVO baseVO)
			throws BaseBusinessException;

	/**
	 * Implemente aqui as validações de negócio do seu módulo. As exceptions de
	 * validação de negócio devem ser acumuladas num BaseBusinessException e
	 * depois este deve ser lancado
	 */
	public abstract void validacoesDeNegocioExcluir(ICadastroBaseVO baseVO)
			throws BaseBusinessException;

	/**
	 * Implemente aqui as validações de negócio do seu módulo. As exceptions de
	 * validação de negócio devem ser acumuladas num BaseBusinessException e
	 * depois este deve ser lancado
	 */
	public abstract void validacoesDeNegocioExcluir(Object id)
			throws BaseBusinessException;

}