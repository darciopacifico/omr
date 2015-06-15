package br.com.dlp.framework.jsf;

import java.io.Serializable;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Contrato para provedor de dados para implementações de JazzDataModel.
 * O JazzDataModel provê um mecanismo de paginação para o componente DataTable do RichFaces.
 * <br>
 * <br>
 * A Solução de design recomendada prevê que o próprio JSF ManagedBean implemente o IJazzDataProvider
 * e, através de uma referência de componente de negócio, realize as pesquisas paginadas e ordenadas.
 * 
 * @author t_dpacifico
 *
 */
public interface IJazzDataProvider<B extends IBaseVO<? extends Serializable>> {

	/**
	 * Operação que irá prover o JazzDataModel com o resultado das pesquisas paginadas e ordenadas.
	 * <br><br>
	 * Os argumentos de pesquisa deverão ser providos pela implementação de IJazzDataProvider.
	 * Na solução de design recomendada, utilizando JSF ManagedBean como implementador de IJazzDataProvider,
	 * os argumentos de pesquisa deverá estar contidos no próprio ManagedBean.
	 * 
	 * @param extraArgumentsDTO Parâmetro da operação que contém os
	 * meta-argumentos de pesquisa como paginação e ordenação de dados
	 */
	public List<B> actionPesquisarCached(ExtraArgumentsDTO extraArgumentsDTO);
	
	
	/**
	 * Determina se o registro referente à PK currentPk está disponível
	 * @param currentPk
	 * @return
	 */
	public boolean isRowAvailable(Serializable currentPk);
	
	/**
	 * Conta quantos registros existem no total sem paginação
	 * @return
	 */
	public Long cachedRowCount();

	
	/**
	 * testa se a última pesquisa em cache ainda é válida
	 * @return
	 */
	public abstract boolean isValidRowCountCache();
	
	
	/**
	 * Invalida a última pesquisa em cache 
	 * @return
	 */
	public String invalidateRowCountCache();

	public abstract void setLinhasTotais(Long linhasTotais);

	public abstract Long getLinhasTotais();
	
}
