/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente UsuarioJazzVO
 *
 **/
@Component
public class UsuarioJazzBusinessImpl extends AbstractBusinessImpl<UsuarioJazzVO> implements UsuarioJazzBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	protected UsuarioJazzDAO usuarioJazzDAO;

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author dpacifico
	 * @param nome
 	 * @param login
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(String nome, String login){
		return usuarioJazzDAO.findUsuarioJazzVO(nome, login);
	}

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author dpacifico
	 * @param nome
 	 * @param login
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(String nome, String login, QueryOrder... queryOrders){
		return usuarioJazzDAO.findUsuarioJazzVO(nome, login, queryOrders);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<UsuarioJazzVO> getDao() {
		return usuarioJazzDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public UsuarioJazzVO newVO() {
		return new UsuarioJazzVO();
	}
	
}