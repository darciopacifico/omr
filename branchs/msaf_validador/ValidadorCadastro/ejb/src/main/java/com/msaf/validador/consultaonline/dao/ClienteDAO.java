package com.msaf.validador.consultaonline.dao;

import java.util.List;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;


public interface ClienteDAO {

	/**
	 * Resonsible for create a Client
	 * 
	 * @param Pojo ClienteVO
	 * @date 2009-11-14
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void criarCliente(ClienteVO cliente)throws PersistenciaException;

	/**
	 * Resonsible for search a Client
	 * 
	 * @param Pojo ClienteVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @return a pojo ClienteVO
	 * @roseuid 49184F590220
	 */
	public abstract ClienteVO buscarPorId(Long id)throws PersistenciaException;

	/**
	 * Resonsible for updates a Cliente
	 * 
	 * @param Pojo
	 *            ClienteVO
	 * @date 2009-11-14
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void atualizarCliente(ClienteVO cliente)throws PersistenciaException;

	/**
	 * Resonsible for remove a Cliente
	 * 
	 * @param Pojo ClienteVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void removerCliente(ClienteVO cliente)throws PersistenciaException;

	/**
	 * Resonsible for remove a Cliente
	 * 
	 * @param Pojo ClienteVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return List of Pojo ClienteVO
	 * @roseuid 49184F590220
	 */
	public abstract List buscarTodos()throws PersistenciaException;



}