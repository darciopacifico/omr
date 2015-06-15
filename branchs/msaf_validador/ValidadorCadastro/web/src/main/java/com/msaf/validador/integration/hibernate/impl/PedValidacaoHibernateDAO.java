package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.bean.PesquisaPedidoValidacao;
import com.msaf.validador.integration.hibernate.intf.IPedValidacaoDAO;
import com.msaf.validador.integration.util.Paginacao;

public class PedValidacaoHibernateDAO extends DAOGenericoHibernate<PedValidacaoVO, Long> implements IPedValidacaoDAO {
	
	@SuppressWarnings("unchecked")
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,readOnly=true)
	public List<PedValidacaoVO> listaPedidosValidacaoCliente(PesquisaPedidoValidacao pesquisaPedidoValidacao, Integer pagina) {
		

		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Select p from PedValidacaoVO p where p.clienteFk.idCliente = :idCliente ");
		
		// verifica se é necessário pesquisar por data
		if(pesquisaPedidoValidacao.getDataInicioSolicitacao() != null && pesquisaPedidoValidacao.getDataFimSolicitacao() != null) {
			buffer.append("and p.dataSolicitacao between :dataInicio and :dataFim ");
		}

		buffer.append("order by p.id desc");
		
		Query query = getEntityManager().createQuery(buffer.toString());

		query.setParameter("idCliente", pesquisaPedidoValidacao.getPedidoValidacao().getClienteFk().getIdCliente());
		
		// verifica se é necessário pesquisar por data
		if(pesquisaPedidoValidacao.getDataInicioSolicitacao() != null && pesquisaPedidoValidacao.getDataFimSolicitacao() != null) {
			query.setParameter("dataInicio", pesquisaPedidoValidacao.getDataInicioSolicitacao());
			query.setParameter("dataFim", pesquisaPedidoValidacao.getDataFimSolicitacao());
		}
		
		Paginacao paginacao = new Paginacao();
		
		paginacao.setInicio(pagina == null?0:pagina * super.getMaxResult());
		paginacao.setLimite(super.getMaxResult());
		
		addPaginacaoRestriction(query, paginacao);
		
		List<PedValidacaoVO> pedidos = query.getResultList();
		
		return pedidos;
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,readOnly=true)
	public Long countListaPedidosValidacaoCliente(PesquisaPedidoValidacao pesquisaPedidoValidacao) {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Select count(p) from PedValidacaoVO p where p.clienteFk.idCliente = :idCliente ");
		
		// verifica se é necessário pesquisar por data
		if(pesquisaPedidoValidacao.getDataInicioSolicitacao() != null && pesquisaPedidoValidacao.getDataFimSolicitacao() != null) {
			buffer.append("and p.dataSolicitacao between :dataInicio and :dataFim ");
		}

		Query query = getEntityManager().createQuery(buffer.toString());

		query.setParameter("idCliente", pesquisaPedidoValidacao.getPedidoValidacao().getClienteFk().getIdCliente());
		
		// verifica se é necessário pesquisar por data
		if(pesquisaPedidoValidacao.getDataInicioSolicitacao() != null && pesquisaPedidoValidacao.getDataFimSolicitacao() != null) {
			query.setParameter("dataInicio", pesquisaPedidoValidacao.getDataInicioSolicitacao());
			query.setParameter("dataFim", pesquisaPedidoValidacao.getDataFimSolicitacao());
		}

		return (Long)query.getSingleResult();
	}
	

}
