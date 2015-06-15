package com.msaf.validador.consultaonline.dao.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.msaf.validador.consultaonline.dao.RegistroPessoaDAO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.util.Utils;



/**
 * source: DadosRetornoInstituicaoDAO.java author: Ekler Paulino de Mattos description:
 * Responsible for gets data from Clients tables version: First version creation
 * date: 2009-02-09
 */
public class RegistroPessoaDAOImpl implements RegistroPessoaDAO  {

	@PersistenceContext(unitName=Utils.PERSISTENCE_UTPERSISTENCE_UNIT_NAME)
	private EntityManager em;


	public RegistroPessoaDAOImpl() {
	}


	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#criarRegistroPessoa(persistencia.vos.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#criarRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#criarRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	public void criarRegistroPessoa(PessoaVO registroPessoa) {
		em.persist(registroPessoa);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#buscarDadosRetornoId(java.lang.Long)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarDadosRetornoId(java.lang.Long)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarDadosRetornoId(java.lang.Long)
	 */
	public PessoaVO buscarRegistroPessoaId(Long id) {
		return em.find(PessoaVO.class, id);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#atualizarRegistroPessoa(persistencia.vos.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#atualizarRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#atualizarRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	public void atualizarRegistroPessoa(PessoaVO registroPessoa) {
		em.merge(registroPessoa);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#removerRegistroPessoa(persistencia.vos.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#removerRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#removerRegistroPessoa(com.msaf.validador.consultaonline.solicitacaovalidacao.RegistroPessoaVO)
	 */
	public void removerRegistroPessoa(PessoaVO registroPessoa) {
		PessoaVO ClienteVOToRemove = buscarRegistroPessoaId(registroPessoa.getId());
		em.remove(ClienteVOToRemove);
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.RegistroPessoaDAO#buscarTodos()
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarTodos()
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarTodos()
	 */
	public List buscarTodos() {
		Query query = em.createQuery("select a from RegistroPessoaVO a");
		List list = query.getResultList();
		return list;
	}


	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarPorNome(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAO#buscarPorNome(java.lang.String)
	 */
	public PessoaVO buscarPorNome(String dadosRetornoNome) {

		List<PessoaVO> clienteList = em.createNamedQuery(
				"RegistroPessoaVO.buscarDadosRetornoPorNome").setParameter("nome",
				 dadosRetornoNome).getResultList();

		if (clienteList.isEmpty())
			return null;
		else
			return clienteList.get(0);
	}

}
