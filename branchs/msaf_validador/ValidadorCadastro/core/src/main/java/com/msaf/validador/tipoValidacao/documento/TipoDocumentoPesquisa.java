package com.msaf.validador.tipoValidacao.documento;

import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;

/**
 * Quando uma pesquisa precisar do tipo do documento este deve ser o serviço que sabe resolver qual o tipo de documento 
 * @author ederson
 *
 */
public interface TipoDocumentoPesquisa {

	/**
	 * passa o valor da pesquisa
	 * @param vo
	 */
	public void setPessoa(PessoaVO vo);
	
	
	/**
	 * Retorna o tipo de documento apropriado para a pesquisa
	 * @return
	 */
	public TipoDocumento getTipoDocumento();
	
}
