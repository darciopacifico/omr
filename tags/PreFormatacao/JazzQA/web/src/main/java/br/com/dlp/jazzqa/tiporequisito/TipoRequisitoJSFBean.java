/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente TipoRequisitoVO
 *
 **/
@Component
public class TipoRequisitoJSFBean extends AbstractJSFBeanImpl<TipoRequisitoVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private TipoRequisitoBusiness tipoRequisitoBusiness;

	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	private String msg; 
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String nome;
 	private String descricao;

	/**
	 * Pesquisa lista de TipoRequisitoVO e disponibiliza resultados em tipoRequisitoVOs 
	 */
	public String actionPesquisar(){
	
		this.msg = "Pesquisa realizada com sucesso!";
		List<TipoRequisitoVO> resultados = tipoRequisitoBusiness.findTipoRequisitoVO(nome, descricao);
		setResultados(resultados);

		return "exibePesquisa";
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
 	 * @return descricao
 	 */
	public String getDescricao(){
		return this.descricao;
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
 	 * @param descricao
 	 */
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<TipoRequisitoVO> getBusiness() {
		return this.tipoRequisitoBusiness;
	}
}