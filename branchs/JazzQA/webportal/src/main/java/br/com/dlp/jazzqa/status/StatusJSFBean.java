/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente StatusVO
 *
 **/
@Scope(value="session")
@Component
public class StatusJSFBean extends AbstractJSFBeanImpl<StatusVO> {
	
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
	private StatusBusiness statusBusiness;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String titulo;
	private String descricao;
	
	/**
	 * Pesquisa lista de StatusVO e disponibiliza resultados em statusVOs
	 */
	@Override
	public String actionPesquisar(){
		
		List<StatusVO> resultados = statusBusiness.findStatusVO(titulo, descricao);
		setResultados(resultados);
		
		return "exibePesquisa";
	}
	
	/**
	 * Accessor Method
	 * @return titulo
	 */
	public String getTitulo(){
		return titulo;
	}
	
	/**
	 * Accessor Method
	 * @return descricao
	 */
	public String getDescricao(){
		return descricao;
	}
	
	/**
	 * Mutator Method
	 * @param titulo
	 */
	public void setTitulo(String titulo){
		this.titulo = titulo;
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
	protected IBusiness<StatusVO> getBusiness() {
		return statusBusiness;
	}
}