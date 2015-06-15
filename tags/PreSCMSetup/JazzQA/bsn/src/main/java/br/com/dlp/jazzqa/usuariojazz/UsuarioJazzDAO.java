/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.jazzqa.status.StatusVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente UsuarioJazzVO
 *
 **/
public interface UsuarioJazzDAO extends IDAO<UsuarioJazzVO>{

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author t_dpacifico
	 * @param status
 	 * @param stati
 	 * @param altura
 	 * @param login
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
	 * @returns Coleção de UsuarioJazzVO
	 */
	List<UsuarioJazzVO> findUsuarioJazzVO(StatusVO status, List<StatusVO> stati, double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome);

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author t_dpacifico
	 * @param status
 	 * @param stati
 	 * @param altura
 	 * @param login
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de UsuarioJazzVO
	 */
	List<UsuarioJazzVO> findUsuarioJazzVO(StatusVO status, List<StatusVO> stati, double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, QueryOrder... queryOrders);

	
}

