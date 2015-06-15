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
 * Implementação de Bean JSF para o componente TipoRequisitoVO
 *
 **/
@Scope(value="session")
@Component
public class TipoRequisitoJSFBean extends AbstractJSFBeanImpl<TipoRequisitoVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private TipoRequisitoBusiness tipoRequisitoBusiness;

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private Date dtInclusaoFrom;
 	private Date dtInclusaoTo;
 	private String descricao;
 	private String nome;

	/**
	 * Pesquisa lista de TipoRequisitoVO e disponibiliza resultados em tipoRequisitoVOs 
	 */
	public String actionPesquisar(){
	
		List<TipoRequisitoVO> resultados = tipoRequisitoBusiness.findTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome);
		setResultados(resultados);

		return "exibePesquisa";
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
 	 * @return descricao
 	 */
	public String getDescricao(){
		return this.descricao;
	}
	
 	/**
	 * Accessor Method
 	 * @return nome
 	 */
	public String getNome(){
		return this.nome;
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
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<TipoRequisitoVO> getBusiness() {
		return this.tipoRequisitoBusiness;
	}
}