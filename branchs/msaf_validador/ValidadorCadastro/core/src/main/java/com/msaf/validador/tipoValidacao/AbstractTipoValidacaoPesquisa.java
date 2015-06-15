package com.msaf.validador.tipoValidacao;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;

public abstract class AbstractTipoValidacaoPesquisa implements TipoValidacaoPesquisa {


	public abstract void setTipoValidacao(TipoValidacao tipoValidacao);


	public abstract void setList(List<TipoValidacaoConfig> list);

}
