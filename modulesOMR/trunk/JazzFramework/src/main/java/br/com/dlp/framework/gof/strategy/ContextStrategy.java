/**
 * 
 */
package br.com.dlp.framework.gof.strategy;


/**
 * Interface Strategy de apuracao de particulas 
 * @author darcio
 *
 */
public interface ContextStrategy <P extends Object, R extends Object> {

	/**
	 * Executa a Strategy selecionada
	 * @param payload
	 * @return
	 * @throws StrategyExecutionException 
	 * @throws Exception 
	 */
	public abstract R executeStrategy(P payload) throws Exception;
		
	
}
