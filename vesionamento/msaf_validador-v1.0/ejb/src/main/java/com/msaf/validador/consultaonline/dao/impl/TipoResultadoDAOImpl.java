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

import com.msaf.validador.consultaonline.dao.TipoResultadoDAO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;


	@Transactional
	public class TipoResultadoDAOImpl implements TipoResultadoDAO {

	
		@PersistenceContext
		private EntityManager em;
		

	    public TipoResultadoDAOImpl() {
	    }


		/* (non-Javadoc)
		 * @see persistencia.daos.TipoResultadoVODAO#criarTipoResultadoVO(persistencia.vos.TpResultVO)
		 */
		/* (non-Javadoc)
		 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#criarTipoResultadoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO)
		 */
		public void criarTipoResultadoVO(TpResultVO tipoResultadoVO) throws PersistenciaException{
			em.persist(tipoResultadoVO);
		}

		
		/* (non-Javadoc)
		 * @see persistencia.daos.TipoResultadoVODAO#buscarPorId(java.lang.Long)
		 */
		/* (non-Javadoc)
		 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarPorId(java.lang.Long)
		 */
		public TpResultVO buscarPorId(Long id) throws PersistenciaException{
			return em.find(TpResultVO.class, id);
		}

		
		/* (non-Javadoc)
		 * @see persistencia.daos.TipoResultadoVODAO#atualizarTipoResultadoVO(persistencia.vos.TpResultVO)
		 */
		/* (non-Javadoc)
		 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#atualizarTipoResultadoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO)
		 */
		public void atualizarTipoResultadoVO(TpResultVO tipoResultadoVO) throws PersistenciaException{
			em.merge(tipoResultadoVO);
		}

		/* (non-Javadoc)
		 * @see persistencia.daos.TipoResultadoVODAO#removerTipoResultadoVO(persistencia.vos.TpResultVO)
		 */
		/* (non-Javadoc)
		 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#removerTipoResultadoVO(com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO)
		 */
		public void removerTipoResultadoVO(TpResultVO tipoResultadoVO) throws PersistenciaException{

			TpResultVO TipoResultadoVOVOToRemove = buscarPorId(tipoResultadoVO.getId());

			em.remove(TipoResultadoVOVOToRemove);
		}

		/* (non-Javadoc)
		 * @see persistencia.daos.TipoResultadoVODAO#buscarTodos()
		 */
		/* (non-Javadoc)
		 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarTodos()
		 */
		public List buscarTodos() throws PersistenciaException{
			Query query = em.createQuery("select a from TpResultVO a");
			List list = query.getResultList();
			return list;
		}

		

		
		/* (non-Javadoc)
		 * @see com.msaf.validador.consultaonline.dao.impl.TipoResultado#buscarPorNome(java.lang.String)
		 */
		public TpResultVO buscarPorNome(String tipoResultadoVONome) throws PersistenciaException{

			List<TpResultVO> tipoResultadoVOList = em.createNamedQuery("TpResultVO.buscarTipoResultadoVOPorNome")
					.setParameter("nome", tipoResultadoVONome).getResultList();

			if (tipoResultadoVOList.isEmpty())
				return null;
			else
				return tipoResultadoVOList.get(0);
		}


	}
