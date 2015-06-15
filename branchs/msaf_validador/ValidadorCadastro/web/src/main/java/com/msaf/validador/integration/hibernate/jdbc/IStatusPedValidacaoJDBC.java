package com.msaf.validador.integration.hibernate.jdbc;

import java.util.List;

public interface IStatusPedValidacaoJDBC {

	/**
	 * Recupera satatus dos pedidos validacoes validacoes
	 * @return 
	 */
	abstract List<StatusPediValidacao> getListaStatusPedidoValidacao(
			String idPedValidacao);

}