package com.msaf.validador.tipoValidacao.documento.impl;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.tipoValidacao.documento.TipoDocumentoPesquisa;

public class TipoDocumentoPesquisaReceitaFederal implements TipoDocumentoPesquisa {

	private PessoaVO vo;
	
	@Override
	public TipoDocumento getTipoDocumento() {
		
		if(vo == null) {
			return null;
		}
		
		if(!Util.isEmpty(vo.getCnpj())) {
			
			return TipoDocumento.RECEITA_CNPJ;
			
		}else if(!Util.isEmpty(vo.getCpf())) {
			
			return TipoDocumento.RECEITA_CPF;
		}
		
		
		return null;
	}

	@Override
	public void setPessoa(PessoaVO vo) {
		this.vo = vo;
	}

}
