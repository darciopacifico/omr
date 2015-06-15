package com.msaf.validador.tipoValidacao.regra.impl;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

public class RegraPesquisaIBGE extends AbstractRegra {

	private TipoValidacao tipoValidacao;
	
	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO vo) {

		String estado = null;
		String cidade = null;
		
		/*
		 * percorre os resultados anteriores para verificar se o estado é de SP
		 */
		for (ResulConsVO resulConsVO : resultadosAnteriores) {

			/*
			 * percorre os resultados anteriores
			 */
			for (DadosRetInstVO dados : resulConsVO.getRegistrosRetorno()) {

				if (!Util.isEmpty(dados.getEstado())) {
					estado = dados.getEstado();
				}
				
				if (!Util.isEmpty(dados.getCidade())) {
					cidade = dados.getCidade();
				}
			}
		}		
		
		if(cidade != null && estado != null) {
			
			vo.setCidade(cidade);
			vo.setEstado(estado);
			
			return this.tipoValidacao;
		}
		
		return null;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

}
