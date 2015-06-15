/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.produto;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente ProdutoVO
 *
 **/
@Component
@WebService
public class ProdutoBusinessImpl extends AbstractBusinessImpl<ProdutoVO> implements ProdutoBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private ProdutoDAO produtoDAO;
	
	/**
	 * Pesquisa entidades do tipo ProdutoVO
	 * @author t_dpacifico
	 * @param dsAreaProduto
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param nome
	 * @param obs
	 * @returns Coleção de ProdutoVO
	 */
	public List<ProdutoVO> findProdutoVO(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs){
		return produtoDAO.findProdutoVO(dsAreaProduto, dtInclusaoFrom, dtInclusaoTo, nome, obs);
	}
	
	/**
	 * Pesquisa entidades do tipo ProdutoVO
	 * @author t_dpacifico
	 * @param dsAreaProduto
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param nome
	 * @param obs
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de ProdutoVO
	 */
	public List<ProdutoVO> findProdutoVOOrdered(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs, ExtraArgumentsDTO  metaArgument){
		return produtoDAO.findProdutoVO(dsAreaProduto, dtInclusaoFrom, dtInclusaoTo, nome, obs, metaArgument);
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<ProdutoVO> getDao() {
		return produtoDAO;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	@WebMethod(exclude=true)
	public ProdutoVO newVO() {
		return new ProdutoVO();
	}
	
}