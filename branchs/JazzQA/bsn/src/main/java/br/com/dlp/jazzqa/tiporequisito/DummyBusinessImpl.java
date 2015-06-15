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
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente DummyVO
 *
 **/
@Component
public class DummyBusinessImpl extends AbstractBusinessImpl<DummyVO> implements DummyBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private DummyDAO dummyDAO;

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @returns Coleção de DummyVO
	 */
	public List<DummyVO> findDummyVO(String descricao, String nome, Date dtInclusao){
		return dummyDAO.findDummyVO(descricao, nome, dtInclusao);
	}

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de DummyVO
	 */
	public List<DummyVO> findDummyVOOrdered(String descricao, String nome, Date dtInclusao, ExtraArgumentsDTO  metaArgument){
		return dummyDAO.findDummyVO(descricao, nome, dtInclusao, metaArgument);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<DummyVO> getDao() {
		return dummyDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public DummyVO newVO() {
		return new DummyVO();
	}
	
}