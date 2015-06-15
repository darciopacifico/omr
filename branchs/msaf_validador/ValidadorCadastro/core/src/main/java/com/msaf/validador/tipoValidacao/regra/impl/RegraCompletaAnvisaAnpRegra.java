package com.msaf.validador.tipoValidacao.regra.impl;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

public class RegraCompletaAnvisaAnpRegra extends AbstractRegra {

	private TipoValidacao tipoValidacao;
	
	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO pessoa) {

//		this.interruptPesquisa();
		
		return tipoValidacao;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

}
