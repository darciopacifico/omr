package com.msaf.validador.tipoValidacao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.tipoValidacao.AbstractTipoValidacaoPesquisa;
import com.msaf.validador.tipoValidacao.TipoValidacaoConfig;
import com.msaf.validador.tipoValidacao.documento.TipoDocumentoPesquisa;
import com.msaf.validador.tipoValidacao.regra.exception.InterruptedPesquisaException;

public class TipoValidacaoPesquisaImpl extends AbstractTipoValidacaoPesquisa {

	private static transient Logger log = Logger.getLogger(TipoValidacaoPesquisaImpl.class);
	
	private TipoValidacao tipoValidacao;
	
	private List<TipoValidacaoConfig> list;
	
	private int index = 0;
	
	private TipoValidacao currentTipoValidacao;
	private TipoValidacaoConfig currentTipoValidacaoConfig;
	private PessoaVO pessoa;
	
	
	private void setValores(TipoValidacao tipoValidacao, TipoValidacaoConfig config, PessoaVO pessoa){
		this.currentTipoValidacao = tipoValidacao;
		this.currentTipoValidacaoConfig = config;
		this.pessoa = pessoa;
	}
	
	public void setList(List<TipoValidacaoConfig> list) {
		
		this.list = list;
		
		// zera o indice 
		this.index = 0;
		
		// ordena a lista
		log.info("Ordenando os tipos de pesquisas");
		ordenarLista();
	}

	@Override
	public TipoValidacao getTipoValidacaoRoot() {
		return tipoValidacao;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;		
	}
	
	@Override
	public TipoValidacao getCurrent() {
		return this.currentTipoValidacao;
	}

	@Override
	public TipoValidacao next(List<ResulConsVO> list, PessoaVO vo) {

		TipoValidacao result = null;
		
		try {
			
			// executa a ação pós pesquisa
			if(this.currentTipoValidacaoConfig != null) {
				this.currentTipoValidacaoConfig.aplicarPosPesquisa(list, vo);
			}
			
			// pega a próxima configuração de tipo de validacao 
			TipoValidacaoConfig config = this.getNextTipoValidacaoConfg();
			
			// não tem mais configurações
			if(config == null){
				
				// atribui null aos valores correntes
				setValores(null, null, null);
				return null;
			}
			
			// pega o retorno 
			result = config.getTipoValidacao(list, vo);
			
			this.setValores(result, config, vo);
			
			// se o retorno foi null e ainda tem registro faz a chamada recursiva
			if((result == null) && (this.hasNextTipoValidacaoConfig())){
				return this.next(list, vo);
			}
			
			
			
		}catch(InterruptedPesquisaException e) {
			
			log.error(e.getMessage());
			
			setValores(null, null, null); 
			
			return null;
		}
		
		
		return result;
	}


	private TipoValidacaoConfig getNextTipoValidacaoConfg() {
		
		if(index == this.list.size()) {
			return null;
		}
		
		index++;
		return list.get(index-1);
	}
	
	private boolean hasNextTipoValidacaoConfig() {
		return index < list.size();
	}
	
	/**
	 * Ordena a lista através do atributo ordem
	 */
	private void ordenarLista(){
		// Em ordem crescente 
		Collections.sort (this.list, new Comparator<TipoValidacaoConfig>() {  
			public int compare(TipoValidacaoConfig param1, TipoValidacaoConfig param2) {  
				return param1.getOrdem() < param2.getOrdem() ? -1 : (param1.getOrdem() > param2.getOrdem() ? +1 : 0);  
			} 
		}); 
	}

	@Override
	public TipoDocumento getTipoDocumento() {
		
		if(this.currentTipoValidacaoConfig != null) {
			
			TipoDocumentoPesquisa tipoDocumento = currentTipoValidacaoConfig.getTipoDocumento();
			
			if(tipoDocumento == null){
				return null;
			}
			
			tipoDocumento.setPessoa(this.pessoa);
			return tipoDocumento.getTipoDocumento();
		}
		
		return null;
	}

}
