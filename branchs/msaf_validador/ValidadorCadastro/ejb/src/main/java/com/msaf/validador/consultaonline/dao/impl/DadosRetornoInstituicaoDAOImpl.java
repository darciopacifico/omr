package com.msaf.validador.consultaonline.dao.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.msaf.validador.consultaonline.dao.DadosRetornoInstituicaoDAO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.util.Utils;



/**
 * source: DadosRetornoInstituicaoDAO.java author: Ekler Paulino de Mattos description:
 * Responsible for gets data from Clients tables version: First version creation
 * date: 2009-02-09
 */
public class DadosRetornoInstituicaoDAOImpl implements DadosRetornoInstituicaoDAO {

	@PersistenceContext(unitName=Utils.PERSISTENCE_UTPERSISTENCE_UNIT_NAME)
	private EntityManager em;


	public DadosRetornoInstituicaoDAOImpl() {
		
	}


	/* (non-Javadoc)
	 * @see persistencia.daos.DadosRetornoInstituicaoDAO#criarDadosRetornoInstituicao(persistencia.vos.DadosRetornoInstituicaoVO)
	 */
	public void criarDadosRetornoInstituicao(DadosRetInstVO dadosRetornoInst) throws PersistenciaException{
		//em.persist(dadosRetornoInst.getRegistroPessoaFk());
		em.persist(dadosRetornoInst);

	}

	/* (non-Javadoc)  
	 * @see persistencia.daos.DadosRetornoInstituicaoDAO#buscarDadosRetornoId(java.lang.Long)
	 */
	public DadosRetInstVO buscarDadosRetornoId(Long id) throws PersistenciaException{
		return em.find(DadosRetInstVO.class, id);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.DadosRetornoInstituicaoDAO#atualizarDadosRetornoInstituicao(persistencia.vos.DadosRetornoInstituicaoVO)
	 */
	public void atualizarDadosRetornoInstituicao(DadosRetInstVO dadosRetornoInst) throws PersistenciaException{
		em.merge(dadosRetornoInst);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.DadosRetornoInstituicaoDAO#removerDadosRetornoInstituicao(persistencia.vos.DadosRetornoInstituicaoVO)
	 */
	public void removerDadosRetornoInstituicao(DadosRetInstVO dadosRetornoInst) throws PersistenciaException{

		DadosRetInstVO ClienteVOToRemove = buscarDadosRetornoId(dadosRetornoInst.getPk());

		em.remove(ClienteVOToRemove);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.DadosRetornoInstituicaoDAO#buscarTodos()
	 */
	public List buscarTodos() throws PersistenciaException{
		Query query = em.createQuery("select a from DadosRetornoInstituicaoVO a");
		List list = query.getResultList();
		return list;
	}


	/**
	 * Search a Dados Retorno Instituicao by name
	 * 
	 * @param name of the AIR
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return Return a list of DadosRetornoInstituicaoVO
	 * @roseuid 49184F590220
	 */
	public DadosRetInstVO buscarPorNome(String dadosRetornoNome) throws PersistenciaException{

		List<DadosRetInstVO> clienteList = em.createNamedQuery(
				"DadosRetornoInstituicaoVO.buscarDadosRetornoPorNome").setParameter("nome",
				 dadosRetornoNome).getResultList();

		if (clienteList.isEmpty())
			return null;
		else
			return clienteList.get(0);
	}



}
