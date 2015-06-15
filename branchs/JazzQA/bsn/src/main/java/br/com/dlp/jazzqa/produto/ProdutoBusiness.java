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

import org.apache.cxf.annotations.DataBinding;
import org.apache.cxf.interceptor.OutInterceptors;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente ProdutoVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
@OutInterceptors(interceptors = { "br.com.dlp.framework.wsi.MockWSSOutInterceptor" })
public interface ProdutoBusiness extends IBusiness<ProdutoVO> {
	
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
	@WebMethod
	List<ProdutoVO> findProdutoVO(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs);
	
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
	@WebMethod
	List<ProdutoVO> findProdutoVOOrdered(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs, ExtraArgumentsDTO  metaArgument);
	
}