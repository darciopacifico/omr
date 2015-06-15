package br.com.dlp.framework.dao;

import java.util.List;

import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.vo.ICadastroBaseVO;

public interface IDAO {
	public static final String ORDEM_ID = "id";

	public static final String ORDEM_NOME = "nome";

	public static final String ORDEM_DESCRICAO = "descricao";

	/**
	 * Como default apenas invoca a onInserir dos DAOObservers registrados. Caso
	 * não esteja utilizando DAOObservers, reimplemente este método na sua
	 * extensão de DAO
	 */
	public ICadastroBaseVO inserir(ICadastroBaseVO baseVO) throws DAOException,
			BaseTechnicalError;

	/**
	 * Como default apenas invoca a onAtualizar dos DAOObservers registrados.
	 * Caso não esteja utilizando DAOObservers, reimplemente este método na sua
	 * extensão de DAO
	 */
	public ICadastroBaseVO atualizar(ICadastroBaseVO baseVO)
			throws DAOException, BaseTechnicalError;

	/**
	 * Exclui os objeto do banco de dados Como default apenas invoca a exclusão
	 * dos DAOObservers registrados. Caso não esteja utilizando DAOObservers,
	 * reimplemente este método na sua extensão de DAO
	 */
	public void excluir(ICadastroBaseVO baseVO) throws DAOException,
			BaseTechnicalError;

	/**
	 * Busca um objeto pela chave no banco de dados. Como default procura nos
	 * DAOObservers registrados pelo resultado da consulta.<br>
	 * Caso não esteja utilizando DAOObservers, reimplemente este método na sua
	 * extensão de DAO
	 */
	public ICadastroBaseVO findByPrimaryKey(IPK chave) throws DAOException,
			BaseTechnicalError;

	/**
	 * Testa se existe um com a chave especificada
	 */
	public boolean exists(IPK chave) throws DAOException, BaseTechnicalError;

	/**
	 * Como default invoca os findAll dos DAOObservers registrados e junta todos
	 * as coleções retornadas Caso não esteja utilizando DAOObservers,
	 * reimplemente este método na sua extensão de DAO
	 */
	public List findAll() throws DAOException, BaseTechnicalError;

	/**
	 * O mesmo que findByIdNomeDescricao(Long,String,String), incrementado de
	 * ordenação
	 */
	public List findAll(QueryOrder[] queryOrders) throws DAOException,
			BaseTechnicalError;

	/**
	 * Serve para atribuir uma referencia de IServicelocator ao IDAO
	 */
	public void setServiceLocator(IServiceLocator serviceLocator)
			throws DAOException;
}
