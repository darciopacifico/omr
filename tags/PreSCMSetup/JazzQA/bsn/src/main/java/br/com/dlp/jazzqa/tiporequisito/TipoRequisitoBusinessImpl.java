/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
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
 * Contrado de Business para o componente TipoRequisitoVO
 *
 **/
@Component
@WebService
public class TipoRequisitoBusinessImpl extends AbstractBusinessImpl<TipoRequisitoVO> implements TipoRequisitoBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private TipoRequisitoDAO tipoRequisitoDAO;

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param descricao
 	 * @param nome
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome){
		return tipoRequisitoDAO.findTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome);
	}

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param descricao
 	 * @param nome
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVOOrdered(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, QueryOrder... queryOrders){
		return tipoRequisitoDAO.findTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome, queryOrders);
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