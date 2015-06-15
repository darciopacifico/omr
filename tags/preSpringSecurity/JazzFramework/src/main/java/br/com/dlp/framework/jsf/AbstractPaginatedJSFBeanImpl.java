/**
 * 
 */
package br.com.dlp.framework.jsf;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * TODO: Corrigir problema das tabelas editaveis (QuestionnaireCRUD), onde os campos do submodulo não atualizando o bean se não for via ajax:support.
 * Implementação abstrata de JSF Bean, com suporte à paginação
 * @author dpacifico
 */
public abstract class AbstractPaginatedJSFBeanImpl<B extends IBaseVO<? extends Serializable>> extends AbstractJSFBeanImpl<B> implements IJazzDataProvider<B> {
	private static final long serialVersionUID = -5770083259678219532L;
	
	
	
	
	/**
	 * Instância de JazzDataModel para paginação
	 */
	private JazzDataModel<B> jazzDataModel;
	private Collection<SelectItem> opcoesQtdLinhas;
	private Integer linhasPorPagina=10;
	private Long linhasTotais=null;
	
	/**
	 * Sobrescreve método salvar apenas para invalidar cache de contagem de registros
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#actionSalvar()
	 */
	@Override
	public String actionSalvar() throws JazzBusinessException {
		invalidateRowCountCache();
		return super.actionSalvar();
	}
	
	/**
	 * Sobrescreve o método delete apenas para invalidar cache de contagem de registros
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#actionDelete(br.com.dlp.framework.vo.IBaseVO)
	 */
	@Override
	public String actionDelete(final B voBean) {
		return super.actionDelete(voBean);
	}
	
	
	/**
	 * Action para confirma exclusao do registro selecionado
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#deleteConfirm()
	 */
	@Override
	public String deleteConfirm() throws JazzBusinessException {
		invalidateRowCountCache();
		return super.deleteConfirm();
	}
	
	/**
	 * Mantem a contagem de registros em cache, invalidando o mesmo apenas caso os argumentos de pesquisa mudem.
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#cachedRowCount()
	 */
	@Override
	public Long cachedRowCount() {
		if(!isValidRowCountCache()){
			this.linhasTotais = rowCount();
		}
		return this.linhasTotais;
	}
	
	
	/**
	 * Testa se o cache de contagem de registros é válido
	 * @return
	 */
	protected boolean isValidRowCountCache() {
		return linhasTotais!=null;
	}
	
	
	/**
	 * Invalida o último valor cacheado de contagem de registros.
	 * Na solução de design proposta, o cache é válido até que os argumentos de pesquisa sejam modificados.
	 * Caso os argumentos de pesquisa mudem, executar este método.
	 * @return
	 */
	public String invalidateRowCountCache() {
		linhasTotais=null;
		return null;
	}
	
	
	/**
	 * Implementar contagem de registros resultantes da consulta.
	 * Este valor será armazenado em cache até que o cache seja invalidado.
	 * Na solução de design proposta, o cache é válido até que os argumentos de pesquisa sejam modificados.
	 * @return
	 */
	protected abstract Long rowCount();
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#isRowAvailable(java.io.Serializable)
	 */
	@Override
	public boolean isRowAvailable(Serializable currentPk) {
		return currentPk!=null;
		//throw new RuntimeException("método isRowAvailable nao implementado !!!!!!!!!!!!!!!!!");
	}
	
	/**
	 * Retorna a especialização de ExtendedDataModel do RichFaces; JazzDataModel
	 * @return JazzDataModel
	 */
	public JazzDataModel<B> getDataModel(){
		if(this.jazzDataModel==null){
			this.jazzDataModel = new JazzDataModel<B>(this);
		}
		
		return this.jazzDataModel;
	}
	
	
	@Override
	public List<B> getResultados() {
		JazzDataModel<B> dataModel = getDataModel();
		
		List<B> resultados = dataModel.getResultados();
		
		return resultados;
	}
	
	
	/**
	 * Retorna as opções para quantidades de linhas para resultados de pesquisa
	 * @return
	 */
	public Collection<SelectItem> getOpcoesQtdLinhas() {
		if(opcoesQtdLinhas==null){
			opcoesQtdLinhas = new ArrayList<SelectItem>();

			opcoesQtdLinhas.add(new SelectItem(5, "5"));
			opcoesQtdLinhas.add(new SelectItem(11, "10"));
			opcoesQtdLinhas.add(new SelectItem(15, "15"));
			opcoesQtdLinhas.add(new SelectItem(25, "25"));
			opcoesQtdLinhas.add(new SelectItem(40, "40"));
		}
		return opcoesQtdLinhas;
	}
	
	
	public void setOpcoesQtdLinhas(Collection<SelectItem> opcoesQtdLinhas) {
		this.opcoesQtdLinhas = opcoesQtdLinhas;
	}
	
	public Integer getLinhasPorPagina() {
		return this.linhasPorPagina;
	}
	
	public void setLinhasPorPagina(Integer linhasPorPagina) {
		this.linhasPorPagina=linhasPorPagina;
	}
	
	
	/**
	 * Método pesquisar não pode ser utilizado em AbstractPaginatedJSFBeanImpl
	 */
	@Override
	public String actionPesquisar() {
		
		if(log.isDebugEnabled()){
			log.debug("O uso do método actionPesquisar em AbstractPaginatedJSFBeanImpl"+
					" não é autorizada para não quebrar o mecanismo de paginação."+
			" Verifique a solução de design aplicável para pesquisa utilizando JazzDataModel!");
		}
		return null;
	}
	
	/**
	 * Limpa os critérios de pesquisa do módulo. Logo após, invalida o cache de contagem de registros retornados
	 * 
	 * @return
	 */
	@Override
	public String actionLimparPesquisa(){
		invalidateRowCountCache();
		return super.actionLimparPesquisa();
	}
	
	public Long getLinhasTotais() {
		return linhasTotais;
	}
	
	public void setLinhasTotais(Long linhasTotais) {
		this.linhasTotais = linhasTotais;
	}
}
