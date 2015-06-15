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
 * 
 * @author dpacifico
 */
public abstract class AbstractJSFBeanImpl<B extends IBaseVO<? extends Serializable>> implements Serializable {
	
	public static final String EXIBE_PESQUISA = "exibePesquisa";
	
	private static final long serialVersionUID = -5765604081444187595L;
	
	/**
	 * Resultados consulta
	 */
	private List<B> resultados = new ArrayList<B>();
	
	/**
	 * Novo bean (inclusão) ou bean selecionado para edição (alteração).
	 */
	private B tmpVO;
	
	/**
	 * Exclusao de registro
	 * 
	 * @param voBean
	 * @return
	 * @throws BaseBusinessException
	 */
	public String actionDelete(final B voBean) throws BaseBusinessException {
		final IBusiness<B> business = this.getBusiness();
		business.delete(voBean);
		this.actionPesquisar();
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	/**
	 * Implementação genérica para botão "Novo". Prepara estado da tela para criação de um novo registro.
	 * 
	 * @return
	 */
	public String actionNovo() {
		final IBusiness<B> business = this.getBusiness();
		this.tmpVO = business.newVO();
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	public String actionPesquisar() {
		final IBusiness<B> business = this.getBusiness();
		this.resultados = business.findAll();
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	public String actionSalvar() throws BaseBusinessException {
		final IBusiness<B> business = this.getBusiness();
		business.saveOrUpdate(this.tmpVO);
		
		this.tmpVO = null;
		
		this.actionPesquisar();
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	/**
	 * Preparar para alteracao
	 * 
	 * @param voBean
	 * @return
	 * @throws BaseBusinessException
	 */
	public String actionUpdate(final B voBean) throws BaseBusinessException {
		this.tmpVO = voBean;
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	/**
	 * Retorna o BusinessObject da implementação concreta do módulo
	 * 
	 * @return IBusiness implementation
	 */
	protected abstract IBusiness<B> getBusiness();
	
	public List<B> getResultados() {
		return this.resultados;
	}
	
	public B getTmpVO() {
		if (this.tmpVO == null) {
			this.tmpVO = this.getBusiness().newVO();
		}
		return this.tmpVO;
	}
	
	public void setResultados(final List<B> resultados) {
		this.resultados = resultados;
	}
	
	public void setTmpVO(final B tmpVO) {
		this.tmpVO = tmpVO;
	}
	
}
