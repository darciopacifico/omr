/**
 * 
 */
package br.com.dlp.jazzomr.person;

import br.com.dlp.jazzomr.converter.AbstractJSONConverter;

/**
 * Converson json para pessoavo. Mera formalidade
 * @author darcio
 *
 */
public class PessoaJSONConverter extends AbstractJSONConverter {

	public PessoaJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return PessoaVO.class;
	}

}
