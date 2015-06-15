package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.intf.IPedValidacaoDAO;

public class PedValidacaoHibernateDAO extends DAOGenericoHibernate<PedValidacaoVO, Long> implements IPedValidacaoDAO {
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,readOnly=true)
	public List<PedValidacaoVO> listaPedidosValidacaoCliente(final ClienteVO cliente) {
		
		Query query = getEntityManager().createQuery("Select p from PedValidacaoVO p where p.clienteFk.idCliente = :idCliente order by p.id desc ");

		 
		
		query.setMaxResults(91);
		 
		query.setParameter("idCliente", cliente.getIdCliente());
		
		List<PedValidacaoVO> pedidos = query.getResultList();
		
		 
		
		
		return pedidos;
	}
	
	
	@Override
	public PedValidacaoVO findById(Long id) {
		// TODO Auto-generated method stub
		
		
		
		EntityManager entityManager = getEntityManager();
		
		
		Query query = entityManager.createQuery(" select p from PedValidacaoVO p left join fetch p.pessoaFK where p.id = :id  ");
		   
		query.setParameter("id", id);
		List<PedValidacaoVO> pedidos = query.getResultList();
		
		PedValidacaoVO pedValidacaoVO=null;
		
		if(pedidos!=null && pedidos.size()>0){
			pedValidacaoVO = pedidos.get(0);
		}
		
		
		//PedValidacaoVO pedValidacaoVO = entityManager.find(getPersistentClass(), id);
		//System.out.println(pedValidacaoVO.getTpValidacoes());
		//System.out.println(pedValidacaoVO.getPessoaFK());
		
		//return super.findById(id);
		return pedValidacaoVO;
	}
}
