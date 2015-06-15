/**
 * 
 */
package br.com.dlp.framework.gof.strategy;


/**
 * Interface para implementacao de strategy
 * @author darcio
 */
public interface Strategy <P extends Object, R extends Object> {
	
	
	/**
	 * Operacao de execucao de strategy
	 * @param payload
	 * @return
	 * @throws StrategyExecutionException 
	 */
	public R execute (P payload) throws Exception;

	
	/**
	 * Ordem ou prioridade de execucao
	 * @return
	 */
	public int getOrder();
	
}
