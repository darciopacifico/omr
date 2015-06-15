/*
 * Created on 06/07/2005
 *
 */
package br.com.dlp.framework.dao;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.ICadastroFacade;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.util.properties.PropertiesFactory;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;
import br.com.dlp.framework.vo.ICadastroBaseVO;

/**
 * Implementação abstrata de IDAO para DAOs JDBC
 */
public abstract class AbstractJDBCDAO extends AbstractDAO {
	protected Log logger = LogFactory.getLog(AbstractJDBCDAO.class);

	public static final String PREFIX_COMPONENTE_EXTERNO = "CompExt_";

	protected int INITIAL_LIST_SIZE = 30;

	/**
	 * Implemente este método no nivel de abstracao do DAO abstrato do seu
	 * projeto<br>
	 * Retorna o JNDI name do DataSource registrado no appServer
	 */
	protected abstract String getDefaultDataSouceName();

	/**
	 * @throws BaseTechnicalError
	 */
	protected Connection getConnection() throws BaseTechnicalError {
		String dataSouceName = getDefaultDataSouceName();

		Connection connection = getConnection(dataSouceName);

		return connection;
	}

	/**
	 * @throws BaseTechnicalError
	 */
	protected Connection getConnection(String dataSouceName)
			throws BaseTechnicalError {
		IServiceLocator serviceLocator = getServiceLocator();

		DataSource dataSource = serviceLocator.getDataSource(dataSouceName);

		Connection connection;
		try {
			connection = dataSource.getConnection();

		} catch (SQLException e) {
			throw new DAOException(
					"Nao foi possivel recuperar a conexao para o dataSouceName:"
							+ dataSouceName, e);

		}
		return connection;
	}

	protected void closeConnection(Connection connection, Statement statement,
			ResultSet resultSet) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fechando conexao, statement e resultset");
		}

		try {
			if (resultSet != null) {
				resultSet.close();
			} else {
				logger
						.warn("Atencao!! O ResultSet informado para ser fechado é nulo!");
			}
		} catch (Exception e) {
			logger.error("ATENCAO!! ERRO AO TENTAR FECHAR O ResultSet:"
					+ e.getMessage());
			e.printStackTrace();

		}

		closeConnection(connection, statement);

	}

	protected void closeConnection(Connection connection, Statement statement) {
		if (logger.isDebugEnabled()) {
			logger.debug("Fechando conexao, statement e resultset");
		}

		try {
			if (statement != null) {
				statement.close();
			} else {
				logger
						.warn("Atencao!! O Statement informado para ser fechado é nulo!");
			}
		} catch (Exception e) {
			logger.error("ATENCAO!! ERRO AO TENTAR FECHAR O Statement:"
					+ e.getMessage());
			e.printStackTrace();
		}
		try {
			if (connection != null) {
				connection.close();
			} else {
				logger
						.warn("Atencao!! O Connection informado para ser fechado é nulo!");
			}
		} catch (Exception e) {
			logger.error("ATENCAO!! ERRO AO TENTAR FECHAR A Connection:"
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Itera um ResultSet utilizando o metodo abstrato getCadastroBaseVO
	 * 
	 * @throws DAOException
	 * @throws SQLException
	 */
	protected List getCadastroBaseVOs(ResultSet rs) throws DAOException {
		List list = new ArrayList(INITIAL_LIST_SIZE);
		try {
			while (rs.next()) {
				list.add(getCadastroBaseVO(rs));
			}
		} catch (SQLException e) {
			throw new DAOException(
					"Não foi possivel gerar o VO para a linha corrente do ResultSet",
					e);

		}

		return list;
	}

	/**
	 * Retorna o VO do item atual do resultSet <br>
	 * Atencao!!! O ResultSet !!NÃO!! pode estar posicionado em EOF ou BOF
	 * 
	 * @throws DAOException
	 */
	protected ICadastroBaseVO getCadastroBaseVO(ResultSet rs)
			throws DAOException {
		Class classeDoVO = getMappedClass();
		ICadastroBaseVO cadastroBaseVO = generateEmptyVO(classeDoVO);
		return cadastroBaseVO;
	}

	protected IDAOFactory getDAOFactory() throws DAOFactoryException {
		try {
			IServiceLocator serviceLocator = getServiceLocator();

			if (serviceLocator == null) {
				throw new DAOFactoryException(
						"Nao foi possível recuperar o DAOFactory, a instancia de ServiceLocator e nula!");
			}

			IDAOFactory daoFactory = serviceLocator.getDAOFactory();

			if (daoFactory == null) {
				throw new DAOFactoryException("O DAOFactory recuperado e nulo!");
			}

			return daoFactory;

		} catch (ServiceLocatorException e) {
			throw new DAOFactoryException(
					"Nao foi possível recuperar o DAOFactory a partir da instancia de ISreviceLocator deste DAO!",
					e);

		}
	}

	/**
	 * Retorna o DAO do módulo informado
	 * 
	 * @throws DAOFactoryException
	 */
	protected IDAO getDAO(String moduleId) throws DAOFactoryException {
		IDAOFactory daoFactory = getDAOFactory();
		IDAO idao = daoFactory.getDAO(moduleId);
		return idao;
	}

	/**
	 * Retorna o DAO do módulo informado
	 * 
	 * @throws DAOFactoryException
	 */
	protected ICadastroFacade getFacade(String moduleId)
			throws DAOFactoryException {
		IServiceLocator serviceLocator;
		ICadastroFacade cadastroFacade;
		try {
			serviceLocator = getServiceLocator();
			cadastroFacade = (ICadastroFacade) serviceLocator
					.getFacade(moduleId);
		} catch (ServiceLocatorException e) {
			throw new DAOFactoryException(
					"Erro ao tentar recuperar facade para o modulo " + moduleId,
					e);

		}
		return cadastroFacade;
	}

	/**
	 * Retorna o ICadastroBaseVO de acordo com o modulo informado mais a IPK
	 * 
	 * @throws BaseTechnicalError
	 * @throws DAOException
	 */
	protected ICadastroBaseVO getCadastroBaseVO(String moduleId, IPK ipk)
			throws DAOException {
		ICadastroBaseVO cadastroBaseVO = null;

		try {
			ICadastroFacade cadastroFacade = getFacade(moduleId);
			cadastroBaseVO = cadastroFacade.findByPrimaryKey(ipk);

		} catch (DAOFactoryException e) {
			throw new DAOException(
					"Erro ao tentar recuperar o facade para o modulo "
							+ moduleId, e);

		} catch (BaseBusinessException e) {
			throw new DAOException(
					"Erro ao tentar recuperar o facade para o modulo "
							+ moduleId, e);

		} catch (BaseTechnicalError e) {
			throw new DAOException(
					"Erro ao tentar recuperar o facade para o modulo "
							+ moduleId, e);

		} catch (RemoteException e) {
			throw new DAOException(
					"Erro ao tentar recuperar o facade para o modulo "
							+ moduleId, e);

		}

		return cadastroBaseVO;
	}

	/**
	 * Avalia se o modulo informado e um componente externo ao projeto
	 */
	protected boolean isComponenteExterno(String moduleId) throws DAOException {
		PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();
		boolean returnValue = false;
		Class class1 = null;

		IDAOFactory daoFactory;
		try {
			daoFactory = getDAOFactory();
			class1 = daoFactory.getClass();
			Properties properties = propertiesFactory.getProperties(class1);
			returnValue = properties.containsKey(PREFIX_COMPONENTE_EXTERNO
					+ moduleId);

		} catch (DAOFactoryException e) {
			throw new DAOException("Erro ao tentar avaliar se o modulo '"
					+ moduleId + "' é um modulo externo", e);

		} catch (PropertiesFactoryException e) {
			throw new DAOException("Erro ao tentar avaliar se o modulo '"
					+ moduleId + "' é um modulo externo", e);

		}

		return returnValue;
	}

	/**
	 * Retorne aqui a classe do VO deste DAO
	 */
	protected abstract Class getMappedClass();

	/**
	 * Cria e retorna um VO vazio a partir de getMappedClass(), o VO criado é
	 * populado com dados de um ResultSet. Este método não é utilizado para
	 * criação de um novo registro.
	 * 
	 * @throws DAOException
	 */
	protected ICadastroBaseVO generateEmptyVO(Class mappedClass)
			throws DAOException {
		ICadastroBaseVO cadastroBaseVO;
		try {
			cadastroBaseVO = (ICadastroBaseVO) FrameworkAcheUtil
					.instanciaObjeto(mappedClass, new Class[] {},
							new Object[] {});

		} catch (FrameworkAcheUtilError e) {
			throw new DAOException(
					"Não foi possivel criar uma instancia de VO a partir da classe informada:"
							+ mappedClass.getName(), e);
		}

		return cadastroBaseVO;
	}

	/**
	 * Implemente aqui a atualizacao do VO apos a inserção ou atualização
	 */
	protected ICadastroBaseVO updateVO(ICadastroBaseVO cadastroBaseVO)
			throws DAOException, BaseTechnicalError {
		return cadastroBaseVO;
	}

	/**
	 * Implementar este método appendando a query básica deste módulo, sem
	 * where, sem ordenacao ex: <br>
	 * SELECT id, cgcCpf, zubalele, nome, observacao, dtCriacao, dtAlteracao,
	 * statusVO from cliente
	 * 
	 */
	// protected abstract void appendBaseQuery(StringBuffer sbQuery);
	/**
	 * Apenda as decisoes de ordenação do DAO
	 */
	// protected abstract void appendOrderClause(StringBuffer sbQuery,
	// QueryOrder[] queryOrders);
}
