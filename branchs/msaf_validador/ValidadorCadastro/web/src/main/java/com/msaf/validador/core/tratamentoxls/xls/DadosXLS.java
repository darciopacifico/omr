package com.msaf.validador.core.tratamentoxls.xls;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;

public interface DadosXLS {

	public List<Column> getValuesHeader();
	
	public List<Column> getValuesBody();
	
	public void setParameter(PessoaVO pessoa, DadosRetInstVO vo);

	public TipoValidacao getTipoValidacao();
}
