package br.com.dlp.framework.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementação abstrata de  IJazzDataProvider para manter controle de cache de resultados
 * @author darcio
 *
 * @param <B>
 */
public abstract class AbstractDataProvider<B extends IBaseVO<? extends Serializable>> implements IJazzDataProvider<B> {

	private Long linhasTotais=null;

	

	
	private List<B> resultadosPesquisa= new ArrayList<B>(0); 
	
	/**
	 * Executar pesquisa em banco de dados 
	 * @param extraArgumentsDTO
	 * @return
	 */
	public abstract List<B> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO);
	
	/**
	 * Avalia estado do cache e determina se a pesquisa em banco de dados deve ser executada ou não
	 */
	public final List<B> actionPesquisarCached(ExtraArgumentsDTO extraArgumentsDTO) {
		
		if(!isValidRowCountCache()){
			this.resultadosPesquisa = actionPesquisar(extraArgumentsDTO);
		}
		
		return resultadosPesquisa;
	}
	
	
	@Override
	public boolean isValidRowCountCache() {
		//return false;
		return linhasTotais !=null && CollectionUtils.isNotEmpty(this.resultadosPesquisa) ;
	}

	@Override
	public Long getLinhasTotais() {
		return linhasTotais;
	}

	@Override
	public void setLinhasTotais(Long linhasTotais) {
		this.linhasTotais = linhasTotais;
	}
	
	public final String invalidateRowCountCache() {
		// TODO Auto-generated method stub
		this.setLinhasTotais(null);
		this.setResultadosPesquisa(null);
		return "";
	}

	
	public List<B> getResultadosPesquisa() {
		return resultadosPesquisa;
	}

	public void setResultadosPesquisa(List<B> resultadosPesquisa) {
		this.resultadosPesquisa = resultadosPesquisa;
	}
	
	
	
	
}
