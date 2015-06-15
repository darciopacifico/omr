/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.produto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente ProdutoVO
 *
 **/
@Scope(value="session")
@Component
public class ProdutoJSFBean extends AbstractJSFBeanImpl<ProdutoVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private ProdutoBusiness produtoBusiness;

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String dsAreaProduto;
 	private Date dtInclusaoFrom;
 	private Date dtInclusaoTo;
 	private String nome;
 	private String obs;

	/**
	 * Pesquisa lista de ProdutoVO e disponibiliza resultados em produtoVOs 
	 */
	public String actionPesquisar(){
	
		List<ProdutoVO> resultados = produtoBusiness.findProdutoVO(dsAreaProduto, dtInclusaoFrom, dtInclusaoTo, nome, obs);
		setResultados(resultados);

		return "exibePesquisa";
	}

	/**
	 * Accessor Method
 	 * @return dsAreaProduto
 	 */
	public String getDsAreaProduto(){
		return this.dsAreaProduto;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtInclusaoFrom
 	 */
	public Date getDtInclusaoFrom(){
		return this.dtInclusaoFrom;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtInclusaoTo
 	 */
	public Date getDtInclusaoTo(){
		return this.dtInclusaoTo;
	}
	
 	/**
	 * Accessor Method
 	 * @return nome
 	 */
	public String getNome(){
		return this.nome;
	}
	
 	/**
	 * Accessor Method
 	 * @return obs
 	 */
	public String getObs(){
		return this.obs;
	}
	
	/**
	 * Mutator Method
 	 * @param dsAreaProduto
 	 */
	public void setDsAreaProduto(String dsAreaProduto){
		this.dsAreaProduto = dsAreaProduto;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtInclusaoFrom
 	 */
	public void setDtInclusaoFrom(Date dtInclusaoFrom){
		this.dtInclusaoFrom = dtInclusaoFrom;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtInclusaoTo
 	 */
	public void setDtInclusaoTo(Date dtInclusaoTo){
		this.dtInclusaoTo = dtInclusaoTo;
	}
	
 	/**
	 * Mutator Method
 	 * @param nome
 	 */
	public void setNome(String nome){
		this.nome = nome;
	}
	
 	/**
	 * Mutator Method
 	 * @param obs
 	 */
	public void setObs(String obs){
		this.obs = obs;
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<ProdutoVO> getBusiness() {
		return this.produtoBusiness;
	}
}