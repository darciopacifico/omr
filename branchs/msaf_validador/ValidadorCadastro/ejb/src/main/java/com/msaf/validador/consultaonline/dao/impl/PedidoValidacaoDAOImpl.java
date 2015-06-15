package com.msaf.validador.consultaonline.dao.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.msaf.validador.consultaonline.dao.PedidoValidacaoDAO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.util.Utils;


/**
 * source: PedidoValidacaoDAO.java 
 * author: Ekler Paulino de Mattos
 * description: Responsible for gets data from Clients tables version: First
 * version creation date: 2009-02-09
 */
public class PedidoValidacaoDAOImpl implements PedidoValidacaoDAO {

	@PersistenceContext(unitName=Utils.PERSISTENCE_UTPERSISTENCE_UNIT_NAME)
	private EntityManager em;
	
    public PedidoValidacaoDAOImpl() {
        
    }

	/* (non-Javadoc)
	 * @see persistencia.daos.PedidoValidacaoDAO#criarPedidoValidacao(persistencia.vos.PedidoValidacaoVO)
	 */
	public void criarPedidoValidacao(PedValidacaoVO pedidoValidacao) throws PersistenciaException{
		em.persist(pedidoValidacao);
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.PedidoValidacaoDAO#buscarPorId(java.lang.Long)
	 */
	public PedValidacaoVO buscarPorId(Long id) throws PersistenciaException{
		return em.find(PedValidacaoVO.class, id);
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.PedidoValidacaoDAO#atualizarPedidoValidacao(persistencia.vos.PedidoValidacaoVO)
	 */
	public void atualizarPedidoValidacao(PedValidacaoVO pedidoValidacao) throws PersistenciaException{
		em.merge(pedidoValidacao);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.PedidoValidacaoDAO#removerPedidoValidacao(persistencia.vos.PedidoValidacaoVO)
	 */
	public void removerPedidoValidacao(PedValidacaoVO pedidoValidacao)throws PersistenciaException {

		PedValidacaoVO PedidoValidacaoVOToRemove = buscarPorId(pedidoValidacao.getId());

		em.remove(PedidoValidacaoVOToRemove);
	}
 
	/* (non-Javadoc)
	 * @see persistencia.daos.PedidoValidacaoDAO#buscarTodos()
	 */
	public List buscarTodos() throws PersistenciaException{
		Query query = em.createQuery("select a from PedidoValidacaoVO where rownum <=10 a order by a.id");
		List list = query.getResultList();
		return list;
	}
 
	

	
	/**
	 * Search a PedidoValidacao by AirName
	 * 
	 * @param name of the pedido validacao
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return Return a list of runnable Jobs
	 * @roseuid 49184F590220
	 */
	public PedValidacaoVO buscarPorNome(String pedidoValidacao) throws PersistenciaException {

		List<PedValidacaoVO> clienteList = em.createNamedQuery("PedidoValidacaoVO.buscarPedidoValidacaoPorNome")
				.setParameter("nome", pedidoValidacao).getResultList();

		if (clienteList.isEmpty())
			return null;
		else
			return clienteList.get(0);
	}


}
