/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzav.person;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzav.StatusEnum;
import br.com.dlp.jazzav.exam.IJazzOMRDAO;
import br.com.dlp.jazzav.person.PessoaVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente PessoaVO
 *
 **/
public interface PessoaDAO extends IJazzOMRDAO<PessoaVO>{
 


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
	List<PessoaVO> findPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

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
	Long countPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

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
	List<PessoaVO> findPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);

	
}

