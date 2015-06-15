/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente TipoRequisitoVO
 *
 **/
@Component
public class TipoRequisitoBusinessImpl extends AbstractBusinessImpl<TipoRequisitoVO> implements TipoRequisitoBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	protected TipoRequisitoDAO tipoRequisitoDAO;

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author dpacifico
	 * @param nome
 	 * @param descricao
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(String nome, String descricao){
		return tipoRequisitoDAO.findTipoRequisitoVO(nome, descricao);
	}

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author dpacifico
	 * @param nome
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(String nome, String descricao, QueryOrder... queryOrders){
		return tipoRequisitoDAO.findTipoRequisitoVO(nome, descricao, queryOrders);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<TipoRequisitoVO> getDao() {
		return tipoRequisitoDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public TipoRequisitoVO newVO() {
		return new TipoRequisitoVO();
	}
	
}