package com.msaf.validador.consultaonline.dao;

import java.util.List;

import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;



public interface DadosRetornoInstituicaoDAO {

	/**
	 * Resonsible for create a Client
	 * 
	 * @param Pojo
	 *            DadosRetornoInstituicaoVO
	 * @date 2009-11-14
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void criarDadosRetornoInstituicao(
			DadosRetInstVO dadosRetornoInst)throws PersistenciaException;

	/**
	 * Resonsible for search a one dados retorno instituicao.
	 * 
	 * @param Pojo
	 *            DadosRetornoInstituicaoVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @return a pojo DadosRetornoInstituicaoVO
	 * @roseuid 49184F590220
	 */
	public abstract DadosRetInstVO buscarDadosRetornoId(Long id)throws PersistenciaException;

	/**
	 * Resonsible for updates a Dados Retorno Instituicao
	 * 
	 * @param Pojo DadosRetornoInstituicaoVO
	 * @date 2009-11-14
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void atualizarDadosRetornoInstituicao(
			DadosRetInstVO dadosRetornoInst)throws PersistenciaException;

	/**
	 * Resonsible for remove a Dados Retorno Instituicao
	 * 
	 * @param Pojo DadosRetornoInstituicaoVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @return
	 * @roseuid 49184F590220
	 */
	public abstract void removerDadosRetornoInstituicao(
			DadosRetInstVO dadosRetornoInst)throws PersistenciaException;

	/**
	 * Resonsible for remove a DadosRetornoInstituicaoVO
	 * 
	 * @param Pojo DadosRetornoInstituicaoVO
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return List of Pojo DadosRetornoInstituicaoVO
	 * @roseuid 49184F590220
	 */
	public abstract List buscarTodos()throws PersistenciaException;



}