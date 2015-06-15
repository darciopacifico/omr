package br.com.dlp.framework.jsf;

import java.io.Serializable;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Contrato para provedor de dados para implementa��es de JazzDataModel.
 * O JazzDataModel provê um mecanismo de pagina��o para o componente DataTable do RichFaces.
 * <br>
 * <br>
 * A Solu��o de design recomendada prevê que o pr�prio JSF ManagedBean implemente o IJazzDataProvider
 * e, atrav�s de uma referência de componente de neg�cio, realize as pesquisas paginadas e ordenadas.
 * 
 * @author t_dpacifico
 *
 */
public interface IJazzDataProvider<B extends IBaseVO<? extends Serializable>> {

	/**
	 * Opera��o que ir� prover o JazzDataModel com o resultado das pesquisas paginadas e ordenadas.
	 * <br><br>
	 * Os argumentos de pesquisa dever�o ser providos pela implementa��o de IJazzDataProvider.
	 * Na solu��o de design recomendada, utilizando JSF ManagedBean como implementador de IJazzDataProvider,
	 * os argumentos de pesquisa dever� estar contidos no pr�prio ManagedBean.
	 * 
	 * @param extraArgumentsDTO Par�metro da opera��o que cont�m os
	 * meta-argumentos de pesquisa como pagina��o e ordena��o de dados
	 */
	public List<B> actionPesquisarCached(ExtraArgumentsDTO extraArgumentsDTO);
	
	
	/**
	 * Determina se o registro referente à PK currentPk est� disponível
	 * @param currentPk
	 * @return
	 */
	public boolean isRowAvailable(Serializable currentPk);
	
	/**
	 * Conta quantos registros existem no total sem pagina��o
	 * @return
	 */
	public Long cachedRowCount();

	
	/**
	 * testa se a �ltima pesquisa em cache ainda � v�lida
	 * @return
	 */
	public abstract boolean isValidRowCountCache();
	
	
	/**
	 * Invalida a �ltima pesquisa em cache 
	 * @return
	 */
	public String invalidateRowCountCache();

	public abstract void setLinhasTotais(Long linhasTotais);

	public abstract Long getLinhasTotais();
	
}
