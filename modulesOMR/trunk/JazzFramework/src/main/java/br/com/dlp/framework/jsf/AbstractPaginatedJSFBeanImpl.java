/**
 * 
 */
package br.com.dlp.framework.jsf;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * TODO: Corrigir problema das tabelas editaveis (QuestionnaireCRUD), onde os campos do submodulo n�o atualizando o bean se n�o for via ajax:support.
 * Implementa��o abstrata de JSF Bean, com suporte à pagina��o
 * @author dpacifico
 */
public abstract class AbstractPaginatedJSFBeanImpl<B extends IBaseVO<? extends Serializable>> extends AbstractJSFBeanImpl<B> implements IJazzDataProvider<B> {
	private static final long serialVersionUID = -5770083259678219532L;
	
	
	private List<B> resultadosPesquisa= new ArrayList<B>(0); 

	/**
	 * Executar pesquisa em banco de dados 
	 * @param extraArgumentsDTO
	 * @return
	 */
	public abstract List<B> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO);
	
	
	/**
	 * Avalia estado do cache e determina se a pesquisa em banco de dados deve ser executada ou n�o
	 */
	public final List<B> actionPesquisarCached(ExtraArgumentsDTO extraArgumentsDTO) {
		
		if(!isValidRowCountCache()){
			this.resultadosPesquisa = actionPesquisar(extraArgumentsDTO);
		}
		
		return resultadosPesquisa;
	}
	
	
	
	/**
	 * Inst�ncia de JazzDataModel para pagina��o
	 */
	private JazzDataModel<B> jazzDataModel;
	private Collection<SelectItem> opcoesQtdLinhas;
	private Integer linhasPorPagina=10;
	private Long linhasTotais=null;
	
	/**
	 * Sobrescreve m�todo salvar apenas para invalidar cache de contagem de registros
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#actionSalvar()
	 */
	@Override
	public String actionSalvar() throws JazzBusinessException {
		invalidateRowCountCache();
		return super.actionSalvar();
	}
	
	/**
	 * Sobrescreve o m�todo delete apenas para invalidar cache de contagem de registros
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
	public String deleteConfirm() {
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
	 * Testa se o cache de contagem de registros � v�lido
	 * @return
	 */
	@Override
	public boolean isValidRowCountCache() {
		return linhasTotais!=null && CollectionUtils.isNotEmpty(this.resultadosPesquisa) ;
	}
	
	
	/**
	 * Invalida o �ltimo valor cacheado de contagem de registros.
	 * Na solu��o de design proposta, o cache � v�lido at� que os argumentos de pesquisa sejam modificados.
	 * Caso os argumentos de pesquisa mudem, executar este m�todo.
	 * @return
	 */
	public String invalidateRowCountCache() {
		linhasTotais=null;
		this.resultadosPesquisa=null;
		return null;
	}
	
	
	/**
	 * Implementar contagem de registros resultantes da consulta.
	 * Este valor ser� armazenado em cache at� que o cache seja invalidado.
	 * Na solu��o de design proposta, o cache � v�lido at� que os argumentos de pesquisa sejam modificados.
	 * @return
	 */
	protected abstract Long rowCount();
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#isRowAvailable(java.io.Serializable)
	 */
	@Override
	public boolean isRowAvailable(Serializable currentPk) {
		return currentPk!=null;
		//throw new RuntimeException("m�todo isRowAvailable nao implementado !!!!!!!!!!!!!!!!!");
	}
	
	/**
	 * Retorna a especializa��o de ExtendedDataModel do RichFaces; JazzDataModel
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
	 * Retorna as op��es para quantidades de linhas para resultados de pesquisa
	 * @return
	 */
	public Collection<SelectItem> getOpcoesQtdLinhas() {
		if(opcoesQtdLinhas==null){
			opcoesQtdLinhas = new ArrayList<SelectItem>();

			opcoesQtdLinhas.add(new SelectItem(5, "5"));
			opcoesQtdLinhas.add(new SelectItem(10, "10"));
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
	 * M�todo pesquisar n�o pode ser utilizado em AbstractPaginatedJSFBeanImpl
	 */
	@Override
	public String actionPesquisar() {
		
		if(log.isDebugEnabled()){
			log.debug("O uso do m�todo actionPesquisar em AbstractPaginatedJSFBeanImpl"+
					" n�o � autorizada para n�o quebrar o mecanismo de pagina��o."+
			" Verifique a solu��o de design aplic�vel para pesquisa utilizando JazzDataModel!");
		}
		return null;
	}
	
	/**
	 * Limpa os crit�rios de pesquisa do m�dulo. Logo ap�s, invalida o cache de contagem de registros retornados
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
