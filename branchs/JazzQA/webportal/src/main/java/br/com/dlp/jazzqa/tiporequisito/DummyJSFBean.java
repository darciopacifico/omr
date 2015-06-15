/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

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
 * Implementação de Bean JSF para o componente DummyVO
 *
 **/
@Scope(value="session")
@Component
public class DummyJSFBean extends AbstractJSFBeanImpl<DummyVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
	
	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[]{"dtInclusaoFrom", "dtInclusaoTo", "descricao","nome","dummyVO"};
	}
	@Autowired
	private DummyBusiness dummyBusiness;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String descricao;
	private String nome;
	private Date dtInclusao;
	
	/**
	 * Pesquisa lista de DummyVO e disponibiliza resultados em dummyVOs
	 */
	@Override
	public String actionPesquisar(){
		
		List<DummyVO> resultados = dummyBusiness.findDummyVO(descricao, nome, dtInclusao);
		setResultados(resultados);
		
		return "exibePesquisa";
	}
	
	/**
	 * Accessor Method
	 * @return descricao
	 */
	public String getDescricao(){
		return descricao;
	}
	
	/**
	 * Accessor Method
	 * @return nome
	 */
	public String getNome(){
		return nome;
	}
	
	/**
	 * Accessor Method
	 * @return dtInclusao
	 */
	public Date getDtInclusao(){
		return dtInclusao;
	}
	
	/**
	 * Mutator Method
	 * @param descricao
	 */
	public void setDescricao(String descricao){
		this.descricao = descricao;
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
	 * @param dtInclusao
	 */
	public void setDtInclusao(Date dtInclusao){
		this.dtInclusao = dtInclusao;
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<DummyVO> getBusiness() {
		return dummyBusiness;
	}
}