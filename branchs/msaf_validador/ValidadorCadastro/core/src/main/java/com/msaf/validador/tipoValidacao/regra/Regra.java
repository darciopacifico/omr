package com.msaf.validador.tipoValidacao.regra;

import java.util.List;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

/**
 * Regras que devem ser aplicadas a uma consulta 
 * @author ederson
 *
 */
public interface Regra {

	/**
	 * aplica a repgra
	 * @param resultadosAnteriores
	 */
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO vo);
	
	
	/**
	 * seta o Tipo de Validacao
	 * @param tipoValidacao
	 */
	public void setTipoValidacao(TipoValidacao tipoValidacao);

	/**
	 * Interronpe o restante da pesquisa
	 */
	public void interruptPesquisa();
	
	/**
	 * Atribui o tipo de documento
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento);
}
