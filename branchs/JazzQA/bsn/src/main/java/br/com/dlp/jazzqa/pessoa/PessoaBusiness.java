/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.pessoa;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente PessoaVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface PessoaBusiness extends IBusiness<PessoaVO> {
	
	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * @author t_dpacifico
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtInc
	 * @param dtAlt
	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	@WebMethod
	List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtInc, Date dtAlt, StatusEnum status);
	
	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * @author t_dpacifico
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtInc
	 * @param dtAlt
	 * @param status
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	@WebMethod
	List<PessoaVO> findPessoaVOOrdered(String login, String email, String nome, String telefone, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO metaArgument);
	
}