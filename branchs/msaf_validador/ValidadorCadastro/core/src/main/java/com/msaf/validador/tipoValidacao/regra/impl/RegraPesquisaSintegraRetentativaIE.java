package com.msaf.validador.tipoValidacao.regra.impl;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

/**
 * Regra que verifica se há a necessidade de pesquisar no sintegra por ie
 * @author ederson
 *
 */
public class RegraPesquisaSintegraRetentativaIE extends AbstractRegra {

	private TipoValidacao tipoValidacao;
	
	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO pessoa) {
		
		// se o tiver Inscrição estadual, e Cnpj
		if((!Util.isEmpty(resultadosAnteriores)) && (pessoa != null) && (!Util.isEmpty(pessoa.getIe())) && (!Util.isEmpty(pessoa.getCnpj()))) {
			
			// deixa apenas com números
			String patern = "[ -._]*";
			String iePesquisa = pessoa.getIe().replace(patern, "");
			String ieResultado = "";
			
			/* percorre os resultados anteriores para verificar se a inscrição estadual informada 
			 * é igual ao retorno obtido na pesquisa anterior.
			 * sendo igual não há a necessidade de realizar uma nova pesquisa 
			 */
			for (ResulConsVO resulConsVO : resultadosAnteriores) {
				
				/*
				 * percorre os resultados anteriores
				 */
				for (DadosRetInstVO dados : resulConsVO.getRegistrosRetorno()) {
					
					ieResultado = dados.getIe();
					
					if(ieResultado != null) {
						
						ieResultado = ieResultado.replaceAll(patern, "");
						
						// verifica se a Inscrição estadual informada é igual ao do resultado obtido na pesquisa
						if(iePesquisa.equals(ieResultado)) {
							
							/**
							 * Retornando null não precisa continuar fazendo mais pesquisas
							 */
							return null;
						}
					}
				}
			}
		}
		
		
		return tipoValidacao;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

}
