/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.pessoa;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente PessoaVO
 *
 **/
@Component
@WebService
public class PessoaBusinessImpl extends AbstractBusinessImpl<PessoaVO> implements PessoaBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private PessoaDAO pessoaDAO;
	
	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * @author t_dpacifico
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtInc
	 * @param dtAlt
	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtInc, Date dtAlt, StatusEnum status){
		return pessoaDAO.findPessoaVO(login, email, nome, telefone, dtInc, dtAlt, status);
	}
	
	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * @author t_dpacifico
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtInc
	 * @param dtAlt
	 * @param status
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVOOrdered(String login, String email, String nome, String telefone, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO metaArgument){
		return pessoaDAO.findPessoaVO(login, email, nome, telefone, dtInc, dtAlt, status, metaArgument);
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<PessoaVO> getDao() {
		return pessoaDAO;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public PessoaVO newVO() {
		return new PessoaVO();
	}
	
}