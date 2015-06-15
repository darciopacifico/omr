/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.IJazzOMRBusiness;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente PessoaVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface PessoaBusiness extends IJazzOMRBusiness<PessoaVO> {

	/**
	 * Pesquisa entidades do tipo PessoaVO 
	 * @author darcio

	 * @param login
 	 * @param email
 	 * @param nome
 	 * @param telefone
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	@WebMethod
	List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);


	/**
	 * Pesquisa entidades do tipo PessoaVO 
	 * @author darcio

	 * @param login
 	 * @param email
 	 * @param nome
 	 * @param telefone
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	@WebMethod
	Long countPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);


	/**
	 * Pesquisa entidades do tipo PessoaVO 
	 * @author darcio

	 * @param login
 	 * @param email
 	 * @param nome
 	 * @param telefone
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	@WebMethod
	List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);


	PessoaVO findPessoa(Principal principal);

	/**
	 * Checa se existe o login informado
	 */
	Boolean existsLogin(String login);


	PessoaVO revogarAcesso(PessoaVO pessoaVO) throws JazzBusinessException;
	
}