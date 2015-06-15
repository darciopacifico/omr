package br.com.dlp.framework.dao;

/**
 * Contrato basico para DAO factory<br>
 * Sr(a) programador(a), comece sua implementação de IDAOFactory por uma
 * implementacao abstrata, AbstractDAOFactory ou AbstractCachedDAOFactory por
 * ex:
 */
public interface IDAOFactory {
	public IDAO getDAO(String nomeModulo) throws DAOFactoryException;
}
