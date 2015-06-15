/**
 * 
 */
package br.com.dlp.framework.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementação abstrata de JSF Bean
 * @author dpacifico
 */
public abstract class AbstractJSFBeanImpl<B extends IBaseVO<? extends Serializable>> implements Serializable {

	private static final long serialVersionUID = -5765604081444187595L;

	/**
	 * Retorna o BusinessObject da implementação concreta do módulo
	 * @return IBusiness implementation
	 */
	protected abstract IBusiness<B> getBusiness();
	
	/**
	 * Novo bean (inclusão) ou bean selecionado para edição (alteração). 
	 */
	private B tmpVO;
	
	/**
	 * Resultados consulta
	 */
	private List<B> resultados = new ArrayList<B>();
	
	
	/**
	 * Implementação genérica para botão "Novo". Prepara estado da tela para criação de um novo registro.
	 * @return
	 */
	public String actionNovo(){
		IBusiness<B> business = getBusiness();
		this.tmpVO = business.newVO();
		return "exibePesquisa";
	}
	
	public String actionSalvar() throws BaseBusinessException{
		IBusiness<B> business = getBusiness();
		business.saveOrUpdate(this.tmpVO);
		
		this.tmpVO=null;
		
		this.actionPesquisar();
		return "exibePesquisa";
	}
	
	
	/**
	 * Exclusao de registro
	 * @param voBean
	 * @return
	 * @throws BaseBusinessException
	 */
	public String actionDelete(B voBean) throws BaseBusinessException{
		IBusiness<B> business = getBusiness();
		business.delete(voBean);
		actionPesquisar();
		return "exibePesquisa";
	}
	
	/**
	 * Preparar para alteracao
	 * @param voBean
	 * @return
	 * @throws BaseBusinessException
	 */
	public String actionUpdate(B voBean) throws BaseBusinessException{
		this.tmpVO = voBean;
		return "exibePesquisa";
	}
	
	
	
	

	
	public String actionPesquisar(){
		IBusiness<B> business = getBusiness();
		this.resultados = business.findAll();
		return "exibePesquisa";
	}

	public B getTmpVO() {
		if(this.tmpVO==null){
			this.tmpVO = getBusiness().newVO();
		}
		return tmpVO;
	}

	public void setTmpVO(B tmpVO) {
		this.tmpVO = tmpVO;
	}

	public List<B> getResultados() {
		return resultados;
	}

	public void setResultados(List<B> resultados) {
		this.resultados = resultados;
	}
	
}
