/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente DummyVO
 *
 **/
public interface DummyDAO extends IDAO<DummyVO>{

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @returns Coleção de DummyVO
	 */
	List<DummyVO> findDummyVO(String descricao, String nome, Date dtInclusao);

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de DummyVO
	 */
	List<DummyVO> findDummyVO(String descricao, String nome, Date dtInclusao, ExtraArgumentsDTO  metaArgument);

	
}

