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

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente UsuarioJazzVO
 *
 **/
public interface UsuarioJazzDAO extends IDAO<UsuarioJazzVO>{

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author dpacifico
	 * @param nome
 	 * @param login
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(String nome, String login);

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author dpacifico
	 * @param nome
 	 * @param login
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(String nome, String login, QueryOrder... queryOrders);

	
}

