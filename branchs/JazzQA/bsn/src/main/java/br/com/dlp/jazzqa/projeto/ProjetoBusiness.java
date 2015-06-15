/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.projeto;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;
import br.com.dlp.jazzqa.projeto.enums.TipoProjetoEnum;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente ProjetoVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface ProjetoBusiness extends IBusiness<ProjetoVO> {

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @returns Coleção de ProjetoVO
	 */
	@WebMethod
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
	@WebMethod
	List<ProjetoVO> findProjetoVOOrdered(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO  metaArgument);
	
}