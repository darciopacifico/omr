package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.Query;

import com.msaf.validador.consultaonline.solicitacaovalidacao.CategoriaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ValorCategoriaVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.intf.IValorCategoriaHibernateDAO;

public class ValorCategoriaHibernateDAO extends DAOGenericoHibernate<ValorCategoriaVO, Long> implements IValorCategoriaHibernateDAO {

	
	@SuppressWarnings("unchecked")
	public List<ValorCategoriaVO> pesquisarValorCategoria(CategoriaVO categoria){
		
		Query query = super.getEntityManager().createQuery("Select v from ValorCategoriaVO v where v.categoria.nome = :nomeCategoria");
		
		query.setParameter("nomeCategoria", categoria.getNome());
		
		return query.getResultList();
	}
	
	public boolean existisValorCategoria(ValorCategoriaVO valorCategoria) {
		
		boolean result = false;
		
		Query query = super.getEntityManager().createQuery("Select v from ValorCategoriaVO v where v.categoria.nome = :nomeCategoria and v.nome = :nomeValor");
		
		query.setParameter("nomeCategoria", valorCategoria.getCategoria().getNome());
		query.setParameter("nomeValor", valorCategoria.getNome());
		
		try{ 
			
			Object valorCategoriaResult = query.getSingleResult();
		
			if(valorCategoriaResult != null) {
				result = true;
			}
			
		}catch(Exception e){
			result = false;
		}
		
		
		return result;
	}
	
}
