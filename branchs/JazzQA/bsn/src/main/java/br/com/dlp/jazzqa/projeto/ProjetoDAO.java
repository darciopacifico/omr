/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.projeto;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;
import br.com.dlp.jazzqa.projeto.enums.TipoProjetoEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente ProjetoVO
 *
 **/
public interface ProjetoDAO extends IDAO<ProjetoVO>{

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @returns Coleção de ProjetoVO
	 */
	List<ProjetoVO> findProjetoVO(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de ProjetoVO
	 */
	List<ProjetoVO> findProjetoVO(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO  metaArgument);

	
}

