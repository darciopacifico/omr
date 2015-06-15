/**
 * 
 */
package br.com.dlp.framework.gof.strategy;

import java.io.Serializable;
import java.util.Map;


/**
 * 
 * Context object generico para a implementacao de um Context Strategy (GoF) para apuracao de particulas  
 * 
 * @author darcio
 */
public class GenericStrategyContext <PK extends Serializable, P extends Object, R extends Object> implements ContextStrategy<P,R>{
	
	private Map<PK,Strategy<P, R>> strategys;
	private Strategy<P,R> selectedStrategy;
	
	/**
	 * Construtor abstrato de Strategy Context. Recebe o mapa com as Strategys possiveis neste contexto
	 * @param strategys
	 */
	public GenericStrategyContext(Map<PK,Strategy<P,R>> strategys){
		this.strategys = strategys;
	}
	
	/**
	 * Construtor abstrato de Strategy Context. Recebe qual e a Strategy padrao
	 * @param strategys
	 * @throws StrategyNotFoundException 
	 */
	public GenericStrategyContext(Map<PK,Strategy<P,R>> strategys, PK defaultStrategy) throws StrategyNotFoundException{
		this(strategys);
		this.loadStrategy(defaultStrategy);
	}

	/**
	 * Executa a estrategia selecionada
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	@Override
	public R executeStrategy(P payload) throws Exception{
		return this.selectedStrategy.execute(payload);
	}
	
	/**
	 * Carrega o strategy selecionado
	 * @param strategyKey
	 * @throws StrategyNotFoundException 
	 */
	public void loadStrategy(PK strategyKey) throws StrategyNotFoundException {
		
		if(strategyKey==null){
			throw new NullPointerException("Informe a chave da Stratey a ser carregada!");
		}
		
		Strategy<P, R> strategy = this.strategys.get(strategyKey);
		
		if(strategy==null){
			throw new StrategyNotFoundException("A Strategy referente a chave "+strategyKey+" nao foi encontrada!");
		}else{
			this.selectedStrategy = strategy;
			
		}
		
	}

	/**
	 * @return the strategys
	 */
	public Map<PK, Strategy<P,R>> getStrategys() {
		return strategys;
	}

	/**
	 * @param strategys the strategys to set
	 */
	public void setStrategys(Map<PK, Strategy<P,R>> strategys) {
		this.strategys = strategys;
	}

	/**
	 * @return the selectedStrategy
	 */
	public Strategy<P,R> getSelectedStrategy() {
		return selectedStrategy;
	}

	/**
	 * @param selectedStrategy the selectedStrategy to set
	 */
	public void setSelectedStrategy(Strategy<P,R> selectedStrategy) {
		this.selectedStrategy = selectedStrategy;
	}
	
	
}
