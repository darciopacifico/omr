package com.msaf.validador.tipoValidacao;

import java.util.List;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

/**
 * Define uma TipoValidacaoPesquisa
 * @author ederson
 *
 */
public interface TipoValidacaoPesquisa {

	/**
	 * seta o tipo de validação root
	 */
	public void setTipoValidacao(TipoValidacao tipoValidacao);

	/**
	 * Retorna o tipo validacao Root
	 * @return enum TipoValidacao
	 */
	public TipoValidacao getTipoValidacaoRoot();

	
	/**
	 * Retorna o próximo Tipo de validação (entity) da estruta de pesquisa
	 * @param list com os resultados já realizados
	 * @return
	 */
	public TipoValidacao next(List<ResulConsVO> list, PessoaVO vo);
	
	/**
	 * retorna o TipoValidacao (entity) corrente
	 * @return
	 */
	public TipoValidacao getCurrent();
	

	/**
	 * Retorna o tipo de documento (se necessário)
	 * @return
	 */
	public TipoDocumento getTipoDocumento();
}
