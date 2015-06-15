/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.jazzqa.status.StatusVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente UsuarioJazzVO
 *
 **/
@Component
@WebService
public class UsuarioJazzBusinessImpl extends AbstractBusinessImpl<UsuarioJazzVO> implements UsuarioJazzBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private UsuarioJazzDAO usuarioJazzDAO;

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
	public List<UsuarioJazzVO> findUsuarioJazzVO(StatusVO status, List<StatusVO> stati, double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome){
		return usuarioJazzDAO.findUsuarioJazzVO(status, stati, altura, login, dtInclusaoFrom, dtInclusaoTo, nome);
	}

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
	public List<UsuarioJazzVO> findUsuarioJazzVOOrdered(StatusVO status, List<StatusVO> stati, double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, QueryOrder... queryOrders){
		return usuarioJazzDAO.findUsuarioJazzVO(status, stati, altura, login, dtInclusaoFrom, dtInclusaoTo, nome, queryOrders);
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