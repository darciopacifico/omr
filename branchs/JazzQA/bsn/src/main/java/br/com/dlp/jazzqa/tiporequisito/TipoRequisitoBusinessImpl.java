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
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;

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
	 * @param dummyVO
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVOSimple(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO){
		return tipoRequisitoDAO.findTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome, dummyVO);
	}
	
	
	
	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param descricao
	 * @param nome
	 * @param dummyVO
	 * @returns Coleção de TipoRequisitoVO
	 */
	@Override
	public List<TipoRequisitoVO> pontoNet(){
		return findTipoRequisitoVO(null,null,"aaa", "aaa",null,null);
	}
	
	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param descricao
	 * @param nome
	 * @param dummyVO
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO, ExtraArgumentsDTO  metaArgument){
		return tipoRequisitoDAO.findTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome, dummyVO, metaArgument);
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
	
	@Override
	public Long countTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO) {
		return tipoRequisitoDAO.countFindTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome, dummyVO);
	}
	
}