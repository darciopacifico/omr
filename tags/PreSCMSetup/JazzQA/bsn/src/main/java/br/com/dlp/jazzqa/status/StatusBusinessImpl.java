/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.status;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente StatusVO
 *
 **/
@Component
@WebService
public class StatusBusinessImpl extends AbstractBusinessImpl<StatusVO> implements StatusBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private StatusDAO statusDAO;

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @returns Coleção de StatusVO
	 */
	public List<StatusVO> findStatusVO(String titulo, String descricao){
		return statusDAO.findStatusVO(titulo, descricao);
	}

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de StatusVO
	 */
	public List<StatusVO> findStatusVOOrdered(String titulo, String descricao, QueryOrder... queryOrders){
		return statusDAO.findStatusVO(titulo, descricao, queryOrders);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<StatusVO> getDao() {
		return statusDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public StatusVO newVO() {
		return new StatusVO();
	}
	
}