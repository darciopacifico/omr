package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.Query;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.intf.IPessoaDAO;

public class PessoaHibernateDAO 
extends DAOGenericoHibernate<PessoaVO, Long> implements IPessoaDAO {
	
	
	
	@SuppressWarnings("unchecked")
	public List<PessoaVO> listByPedidoValidacao(PedValidacaoVO vo) {
		
		Query query = this.getEntityManager().createQuery("Select p from PessoaVO p where p.pedValidFk.id = :idPedidoValidacao");
		
		query.setParameter("idPedidoValidacao", vo.getId());
		
		return query.getResultList();
	}

	
	@SuppressWarnings("unchecked")
	public List<PessoaVO> findByIdPedido(Long idPedido) {
		
		Query query = this.getEntityManager().createQuery(
				"	select ped.pessoaFK " +
				"	from PedValidacaoVO ped " +
				"	left join FETCH ped.pessoaFK " +
				"	where ped.id = :id ");
		
		query.setParameter("id",idPedido);
		
		return query.getResultList();
		
	}
	
}
