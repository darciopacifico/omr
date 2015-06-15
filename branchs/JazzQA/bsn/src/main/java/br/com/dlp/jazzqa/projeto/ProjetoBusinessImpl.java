/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.projeto;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;
import br.com.dlp.jazzqa.projeto.enums.TipoProjetoEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente ProjetoVO
 *
 **/
@Component
@WebService
public class ProjetoBusinessImpl extends AbstractBusinessImpl<ProjetoVO> implements ProjetoBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private ProjetoDAO projetoDAO;

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @returns Coleção de ProjetoVO
	 */
	public List<ProjetoVO> findProjetoVO(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status){
		return projetoDAO.findProjetoVO(tipoProjeto, dtInc, dtAlt, status);
	}

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de ProjetoVO
	 */
	public List<ProjetoVO> findProjetoVOOrdered(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO  metaArgument){
		return projetoDAO.findProjetoVO(tipoProjeto, dtInc, dtAlt, status, metaArgument);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<ProjetoVO> getDao() {
		return projetoDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public ProjetoVO newVO() {
		return new ProjetoVO();
	}
	
}