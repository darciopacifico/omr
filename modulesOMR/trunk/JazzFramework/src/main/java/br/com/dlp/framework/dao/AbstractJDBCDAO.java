/**
 * 
 */
package br.com.dlp.framework.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementação abstrata de DAOs para JDBC, utilizando Spring JdbcDaoSupport
 * 
 * teste snapshot
 * 
 * @author dpacifico
 * @param <P>
 * @param <P>
 * @param <B>
 * 
 */
public abstract class AbstractJDBCDAO<B extends IBaseVO<? extends Serializable>> extends JdbcDaoSupport implements IDAO<B> {
	
	private static final long serialVersionUID = 5730970152568135815L;
	
	/**
	 * Default constructor
	 */
	public AbstractJDBCDAO() {
		// intencionalmente em branco
	}
	
	/**
	 * Retorna a classe referente ao primeiro parameter type definido na implementação de AbstractHibernateDAO
	 * 
	 * @return
	 */
	protected Class getVoClass() {
		final Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		
		return (Class) types[0];
	}
	
	/**
	 * Exclui os objeto do banco de dados Como default apenas invoca a exclusao dos DAOObservers registrados. Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 * 
	 * @param baseVO
	 * @exception JazzBusinessException
	 */
	public void delete(B baseVO) throws JazzBusinessException {
		
	}
	
	/**
	 * Testa se existe um com a chave especificada
	 * 
	 * @param chave
	 */
	public boolean exists(Serializable chave) {
		return false;
	}
	
	/**
	 * Testa se existe um com a chave especificada
	 * 
	 * @param baseVO
	 */
	public boolean exists(B baseVO) {
		return false;
	}
	
	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 * 
	 * @param firstResult
	 * @param maxResult
	 */
	public List<B> findAll(Integer firstResult, Integer maxResult) {
		return null;
	}
	
	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public List<B> findAll() {
		return null;
	}
	
	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 * 
	 * @param chave
	 */
	public B findByPK(Serializable chave) {
		return null;
	}
	
	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 * 
	 * @param baseVO
	 */
	public B findByPK(B baseVO) {
		return null;
	}
	
	/**
	 * Como default apenas invoca a onInserir dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este metodo
	 * na sua extensao de DAO
	 * 
	 * @param baseVO
	 * @exception JazzBusinessException
	 */
	public B saveOrUpdate(B baseVO) throws JazzBusinessException {
		return null;
	}
	
	/**
	 * Como default apenas invoca a onAtualizar dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este
	 * metodo na sua extensao de DAO
	 * 
	 * @param baseVO
	 * @exception JazzBusinessException
	 */
	public B update(B baseVO) throws JazzBusinessException {
		return null;
	}

	
}
