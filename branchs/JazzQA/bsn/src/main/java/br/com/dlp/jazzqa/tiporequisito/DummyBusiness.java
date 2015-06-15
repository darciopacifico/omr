/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.business.IBusiness;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente DummyVO
 *
 **/
public interface DummyBusiness extends IBusiness<DummyVO> {

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @returns Coleção de DummyVO
	 */
	@WebMethod
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
	@WebMethod
	List<DummyVO> findDummyVOOrdered(String descricao, String nome, Date dtInclusao, ExtraArgumentsDTO  metaArgument);
	
}