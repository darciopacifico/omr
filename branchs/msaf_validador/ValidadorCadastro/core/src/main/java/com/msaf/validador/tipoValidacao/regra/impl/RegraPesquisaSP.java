package com.msaf.validador.tipoValidacao.regra.impl;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

public class RegraPesquisaSP extends AbstractRegra {

	private TipoValidacao tipoValidacao;

	private static final String ESTADO_SP = "SP";
	
	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO vo) {

		/*
		 * percorre os resultados anteriores para verificar se o estado é de SP
		 */
		for (ResulConsVO resulConsVO : resultadosAnteriores) {

			/*
			 * percorre os resultados anteriores
			 */
			for (DadosRetInstVO dados : resulConsVO.getRegistrosRetorno()) {

				if (!Util.isEmpty(dados.getEstado())) {

					if (dados.getEstado().trim().equals(ESTADO_SP)) {
						return this.tipoValidacao; // São Paulo
					}
				}
			}
		}

		return null;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

}
