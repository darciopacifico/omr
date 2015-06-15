package com.msaf.validador.consultaonline.dao;

import java.util.List;

import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;

public interface PedidoValidacaoDAO {

	/**
	 * Resonsible for create a Client
	 * 
	 * @param Pojo PedidoValidacaoVO
	 * @date 2009-11-14
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void criarPedidoValidacao(PedValidacaoVO pedidoValidacao) throws PersistenciaException;

	/**
	 * Resonsible for search a Client
	 * 
	 * @param Pojo PedidoValidacaoVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @return a pojo PedidoValidacaoVO
	 * @roseuid 49184F590220
	 */
	public abstract PedValidacaoVO buscarPorId(Long id)throws PersistenciaException;

	/**
	 * Resonsible for updates a PedidoValidacao
	 * 
	 * @param Pojo
	 *            PedidoValidacaoVO
	 * @date 2009-11-14
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void atualizarPedidoValidacao(
			PedValidacaoVO pedidoValidacao)throws PersistenciaException;

	/**
	 * Resonsible for remove a PedidoValidacao
	 * 
	 * @param Pojo PedidoValidacaoVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void removerPedidoValidacao(
			PedValidacaoVO pedidoValidacao)throws PersistenciaException;

	/**
	 * Resonsible for remove a PedidoValidacao
	 * 
	 * @param Pojo PedidoValidacaoVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return List of Pojo PedidoValidacaoVO
	 * @roseuid 49184F590220
	 */
	public abstract List buscarTodos()throws PersistenciaException;


}