package com.msaf.validador.tipoValidacao.documento.impl;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.tipoValidacao.documento.TipoDocumentoPesquisa;

public class TipoDocumentoPesquisaSintegraInscricaoEstadual implements TipoDocumentoPesquisa {

	@Override
	public TipoDocumento getTipoDocumento() {
		return TipoDocumento.SINTEGRA_IE;
	}

	@Override
	public void setPessoa(PessoaVO vo) {
		
	}

}
