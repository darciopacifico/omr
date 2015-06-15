package com.msaf.validador.consultaonline.dao;

import java.util.List;

import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;

public interface TipoResultadoDAO {

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#criarTipoResultadoVO(persistencia.vos.TpResultVO)
	 */
	public abstract void criarTipoResultadoVO(TpResultVO tipoResultadoVO)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#buscarPorId(java.lang.Long)
	 */
	public abstract TpResultVO buscarPorId(Long id)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#atualizarTipoResultadoVO(persistencia.vos.TpResultVO)
	 */
	public abstract void atualizarTipoResultadoVO(TpResultVO tipoResultadoVO)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#removerTipoResultadoVO(persistencia.vos.TpResultVO)
	 */
	public abstract void removerTipoResultadoVO(TpResultVO tipoResultadoVO)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#buscarTodos()
	 */
	public abstract List buscarTodos() throws PersistenciaException;

	/**
	 * Search a TipoResultado by AirName
	 * 
	 * @param name of the AIR
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return Return a list of runnable Jobs
	 * @roseuid 49184F590220
	 */
	public abstract TpResultVO buscarPorNome(String tipoResultadoVONome)
			throws PersistenciaException;

}