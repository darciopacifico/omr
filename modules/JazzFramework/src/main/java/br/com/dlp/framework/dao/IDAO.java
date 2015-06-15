package br.com.dlp.framework.dao;

//br.com.dlp.framework.dao.IDAO
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Contrato básico para implementação de DAOs
 * 
 * @author dpacifico
 * 
 * @param <P>
 * @param <B>
 */
public interface IDAO<B extends IBaseVO<? extends Serializable>> extends Serializable {
	
	/**
	 * Exclui os objeto do banco de dados Como default apenas invoca a exclusao dos DAOObservers registrados. Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	void delete(B baseVO) throws JazzBusinessException;
	
	/**
	 * Testa se existe um com a chave especificada
	 */
	boolean exists(B baseVO);
	
	/**
	 * Testa se existe um com a chave especificada
	 */
	boolean exists(Serializable chave);
	
	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 * @param orderMap
	 * @param maxResult
	 * @param firstResult
	 */
	List<B> findAll(ExtraArgumentsDTO extraArgumentsDTO);
	
	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	List<B> findAll();	
	
	/**
	 * Executa um findAll de todos os registros não contidos na lista informada
	 */
	List<B> findAllNotIn(List<B> notIn, String... fetchProfiles);
	
	/**
	 * Procura por texto livre 
	 * @param textSearch 
	 */
	List<B> findByTextSearch(String textSearch);
	
	
	/**
	 * Faz a busca com os mesmos elementos de 'beans', mas aplicando os fetchprofiles informados 
	 * @param beans
	 * @param fetchProfiles
	 * @return
	 */
	List<B> findBeansByBeans(Collection<B> beans, String... fetchProfiles);
	
	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	B findByPK(B baseVO);
	
	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	B findByPK(Serializable chave);
	
	/**
	 * Como default apenas invoca a onInserir dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este metodo
	 * na sua extensao de DAO
	 */
	B saveOrUpdate(B baseVO) throws JazzBusinessException;
	
	/**
	 * Como default apenas invoca a onAtualizar dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este
	 * metodo na sua extensao de DAO
	 */
	B update(B baseVO) throws JazzBusinessException;
	
}
