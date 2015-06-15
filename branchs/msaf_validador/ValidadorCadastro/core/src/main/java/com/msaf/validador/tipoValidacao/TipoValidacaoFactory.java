package com.msaf.validador.tipoValidacao;

import java.util.List;
import java.util.Map;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.core.Factory;

public class TipoValidacaoFactory {

	private static Map<TipoValidacao, List<TipoValidacaoConfig>> configuracao;
	
	private TipoValidacaoFactory(Map<TipoValidacao, List<TipoValidacaoConfig>> map) {
		configuracao = map;
	}

	
	private static TipoValidacaoFactory instance;
	
	
	public static TipoValidacaoFactory getInstance() {
		
		if(instance == null) {
			instance = (TipoValidacaoFactory) Factory.getBean("tipoValidacaoFactory");
		}
		
		return instance;
	}
	
	
	public TipoValidacaoPesquisa createTipoValidacaoPesquisa(TipoValidacao tipoValidacao) {
		
		
		AbstractTipoValidacaoPesquisa pesquisa = (AbstractTipoValidacaoPesquisa) Factory.getBean("tipoValidacaoPesquisa");
		
		pesquisa.setList(configuracao.get(tipoValidacao));
		pesquisa.setTipoValidacao(tipoValidacao);
		
		return pesquisa;
	}

}
