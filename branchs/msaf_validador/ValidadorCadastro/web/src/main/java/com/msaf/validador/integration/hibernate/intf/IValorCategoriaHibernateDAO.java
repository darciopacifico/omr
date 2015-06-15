package com.msaf.validador.integration.hibernate.intf;

import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.CategoriaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ValorCategoriaVO;
import com.msaf.validador.integration.hibernate.base.IDAOGenerico;

public interface IValorCategoriaHibernateDAO extends IDAOGenerico<ValorCategoriaVO, Long> {

	/**
	 * Lista todos os valores de uma categoria
	 * @param categoria
	 * @return
	 */
	public List<ValorCategoriaVO> pesquisarValorCategoria(CategoriaVO categoria);

	
	/**
	 * Verifica se existe o Valor categoria na base
	 * @param valorCategoria (nome do valorCategoria e nome da Categoria)
	 * @return
	 */
	public boolean existisValorCategoria(ValorCategoriaVO valorCategoria);
}
