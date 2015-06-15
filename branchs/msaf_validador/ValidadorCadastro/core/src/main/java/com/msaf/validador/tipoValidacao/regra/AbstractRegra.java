package com.msaf.validador.tipoValidacao.regra;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.tipoValidacao.regra.exception.InterruptedPesquisaException;


public abstract class AbstractRegra implements Regra {

	private TipoDocumento tipoDocumento;
	
	@Override
	public void interruptPesquisa() {
		throw new InterruptedPesquisaException("Pesquisa Interronpida por não atender a Regra");
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	@Override
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
}
