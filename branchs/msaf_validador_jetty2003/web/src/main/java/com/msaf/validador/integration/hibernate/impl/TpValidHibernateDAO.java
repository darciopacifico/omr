package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.Query;

import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.intf.ITpValidDAO;

public class TpValidHibernateDAO extends DAOGenericoHibernate<TpValidVO, Long> implements ITpValidDAO {

	@Override
	public List<TpValidVO> validacoeComerciais() {
		Query query = getEntityManager().createQuery("Select tpv from TpValidVO tpv where tpv.id in (1,3,899) order by tpv.id desc ");
		List<TpValidVO> listaTpValidacao = query.getResultList();
		return listaTpValidacao;
	}


	


}
