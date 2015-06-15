/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente TipoRequisitoVO
 *
 **/
public interface TipoRequisitoDAO extends IDAO<TipoRequisitoVO>{
	
	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param descricao
	 * @param nome
	 * @param dummyVO
	 * @param orderMap
	 * @returns Coleção de TipoRequisitoVO
	 */
	List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO);
	
	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param descricao
	 * @param nome
	 * @param dummyVO
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO, ExtraArgumentsDTO  metaArgument);
	
	Long countFindTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO);
	
	
}
