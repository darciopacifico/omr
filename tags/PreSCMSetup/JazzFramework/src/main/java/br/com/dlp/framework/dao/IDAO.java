package br.com.dlp.framework.dao;

//br.com.dlp.framework.dao.IDAO
import java.io.Serializable;
import java.util.List;

import br.com.dlp.framework.exception.BaseBusinessException;
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
	void delete(B baseVO) throws BaseBusinessException;
	
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
	 */
	List<B> findAll();
	
	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando
	 * DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	List<B> findAll(Integer firstResult, Integer maxResult);
	
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
	B saveOrUpdate(B baseVO) throws BaseBusinessException;
	
	/**
	 * Como default apenas invoca a onAtualizar dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este
	 * metodo na sua extensao de DAO
	 */
	B update(B baseVO) throws BaseBusinessException;
	
}
