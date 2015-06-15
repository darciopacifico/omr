/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente UsuarioJazzVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface UsuarioJazzBusiness extends IBusiness<UsuarioJazzVO> {

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author t_dpacifico
	 * @param altura
 	 * @param login
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
	 * @returns Coleção de UsuarioJazzVO
	 */
	@WebMethod
	List<UsuarioJazzVO> findUsuarioJazzVO(double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome);

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author t_dpacifico
	 * @param altura
 	 * @param login
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de UsuarioJazzVO
	 */
	@WebMethod
	List<UsuarioJazzVO> findUsuarioJazzVOOrdered(double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, ExtraArgumentsDTO  metaArgument);
	
}