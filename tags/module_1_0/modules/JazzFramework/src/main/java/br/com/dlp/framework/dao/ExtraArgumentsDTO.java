package br.com.dlp.framework.dao;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

/**
 * DTO para transportar escolhas de ordenacao de pesquisas e paginacao
 * 
 * @author t_dpacifico
 */
public class ExtraArgumentsDTO implements Serializable {
	private static final long serialVersionUID = 4490514737914281036L;
	
	//LinkedMap garante que a ordem de entrada seja igual aa ordem de leitura. Imprescindivel para a logica de ordenacao!
	private final Map<String, Boolean> orderMap = new LinkedMap();
	private Integer firstResult=null;
	private Integer maxResults=null;
	
	/**
	 * Apenas para cumprir exigências para geracao de webServices
	 */
	public ExtraArgumentsDTO() {
		// intencionalmente em branco
	}
	
	/**
	 * Atualiza máquina de estados de ordenação para o campo informado no argumento
	 * @param sortedField campo
	 */
	public void processOrderChoice(String sortedField) {
		if(sortedField==null) {
			return;
		}
		
		Boolean ordem = orderMap.get(sortedField);
		if( ordem==null){
			orderMap.put(sortedField,true);
		}else if(ordem){
			orderMap.put(sortedField,false);
		}else {
			orderMap.remove(sortedField);
		}
	}
	
	/**
	 * Mapa para ordenação de campos
	 * @return
	 */
	public Map<String, Boolean> getOrderMap() {
		return orderMap;
	}
	
	/**
	 * @return
	 */
	public Integer getFirstResult() {
		return firstResult;
	}
	
	/**
	 * @return
	 */
	public Integer getMaxResults() {
		return maxResults;
	}
	
	/**
	 * @param firstResult
	 */
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	
	/**
	 * @param maxResults
	 */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	
	/**
	 * Verifica se existem extra argumentos para paginação dos resultados
	 * @return
	 */
	public boolean hasPageArguments() {
		return firstResult!=null && maxResults!=null;
	}
	
	/**
	 * Verifica se existem extra argumentos para ordenação dos resultados
	 * @return
	 */
	public boolean hasOrderArguments() {
		return orderMap!=null && !orderMap.isEmpty();
	}
}