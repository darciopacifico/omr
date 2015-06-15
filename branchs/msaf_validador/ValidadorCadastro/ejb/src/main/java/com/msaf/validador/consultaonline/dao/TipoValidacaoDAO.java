package com.msaf.validador.consultaonline.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;

public interface TipoValidacaoDAO {

	public abstract void criarTipoValidacaoVO(TpValidVO tipoValidacaoVO)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#buscarPorId(java.lang.Long)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#buscarPorId(java.lang.Long)
	 */
	@Transactional
	public abstract TpValidVO buscarPorId(Long id) throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#atualizarTipoValidacaoVO(persistencia.vos.TpValidVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#atualizarTipoValidacaoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	public abstract void atualizarTipoValidacaoVO(TpValidVO tipoValidacaoVO)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#removerTipoValidacaoVO(persistencia.vos.TpValidVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#removerTipoResultadoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	public abstract void removerTipoResultadoVO(TpValidVO tipoValidacaoVO)
			throws PersistenciaException;

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#buscarTodos()
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarTodos()
	 */
	public abstract List buscarTodos() throws PersistenciaException;

	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarPorNome(java.lang.String)
	 */
	public abstract TpValidVO buscarPorNome(String tipoResultadoVONome)
			throws PersistenciaException;

}