package com.msaf.validador.consultaonline.dao.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.dao.TipoValidacaoDAO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;


@Transactional
public class TipoValidacaoDAOImpl implements TipoValidacaoDAO {


	@PersistenceContext
	private EntityManager em;
	

    public TipoValidacaoDAOImpl() {
    }


	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#criarTipoValidacaoVO(persistencia.vos.TpResultVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#criarTipoValidacaoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO)
	 */
    
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacaoDAO#criarTipoValidacaoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	public void criarTipoValidacaoVO(TpValidVO tipoValidacaoVO) throws PersistenciaException{
		em.persist(tipoValidacaoVO);
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#buscarPorId(java.lang.Long)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#buscarPorId(java.lang.Long)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacaoDAO#buscarPorId(java.lang.Long)
	 */
	@Transactional
	public TpValidVO buscarPorId(Long id) throws PersistenciaException{
		return em.find(TpValidVO.class, id);
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#atualizarTipoValidacaoVO(persistencia.vos.TpValidVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#atualizarTipoValidacaoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacaoDAO#atualizarTipoValidacaoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	public void atualizarTipoValidacaoVO(TpValidVO tipoValidacaoVO) throws PersistenciaException{
		em.merge(tipoValidacaoVO);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoValidacaoVODAO#removerTipoValidacaoVO(persistencia.vos.TpValidVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacao#removerTipoResultadoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacaoDAO#removerTipoResultadoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO)
	 */
	public void removerTipoResultadoVO(TpValidVO tipoValidacaoVO) throws PersistenciaException{

		TpValidVO TipoResultadoVOVOToRemove = buscarPorId(tipoValidacaoVO.getId());

		em.remove(TipoResultadoVOVOToRemove);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.TipoResultadoVODAO#buscarTodos()
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarTodos()
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacaoDAO#buscarTodos()
	 */
	public List buscarTodos() throws PersistenciaException{
		Query query = em.createQuery("select a from TpValidVO a");
		List list = query.getResultList();
		return list;
	}

	

	
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarPorNome(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.TipoValidacaoDAO#buscarPorNome(java.lang.String)
	 */
	public TpValidVO buscarPorNome(String tipoResultadoVONome) throws PersistenciaException{

		List<TpValidVO> tipoResultadoVOList = em.createNamedQuery("TpValidVO.buscarTipoValidacaoVOPorNome")
				.setParameter("nome", tipoResultadoVONome).getResultList();

		if (tipoResultadoVOList.isEmpty())
			return null;
		else
			return tipoResultadoVOList.get(0);
	}


}
