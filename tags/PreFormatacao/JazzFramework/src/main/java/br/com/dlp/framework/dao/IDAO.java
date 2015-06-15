package br.com.dlp.framework.dao;
//br.com.dlp.framework.dao.IDAO
import java.io.Serializable;
import java.util.List;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Contrato básico para implementação de DAOs
 * @author dpacifico
 *
 * @param <P>
 * @param <B>
 */
public interface IDAO <B extends IBaseVO<? extends Serializable>> extends Serializable{

	/**
	 * Como default apenas invoca a onInserir dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public B saveOrUpdate(B baseVO) throws BaseBusinessException;

	/**
	 * Como default apenas invoca a onAtualizar dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public B update(B baseVO) throws BaseBusinessException;

	/**
	 * Exclui os objeto do banco de dados Como default apenas invoca a exclusao dos DAOObservers registrados. Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public void delete(B baseVO) throws BaseBusinessException;

	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public B findByPK(Serializable  chave);

	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public B findByPK(B baseVO);

	/**
	 * Testa se existe um com a chave especificada
	 */
	public boolean exists(Serializable chave);

	/**
	 * Testa se existe um com a chave especificada
	 */
	public boolean exists(B baseVO);

	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public List<B> findAll(Integer firstResult, Integer maxResult);

	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos as coleAAes retornadas Caso Nao esteja utilizando DAOObservers, reimplemente este metodo na sua extensao de DAO
	 */
	public List<B> findAll();

}
