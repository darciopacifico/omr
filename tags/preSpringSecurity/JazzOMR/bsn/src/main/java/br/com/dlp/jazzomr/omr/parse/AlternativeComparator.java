/**
 * 
 */
package br.com.dlp.jazzomr.omr.parse;

import ij.measure.ResultsTable;

import java.util.Comparator;

import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Component;

import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.poc.COLUMNS_PARTICLE;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeVO;

/**
 * Compara os resultados de análise de particulas de duas possíveis alternativas. 
 * Prioriza a alternativa com maior possibilidade de ter sido a preenchida pelo aluno.
 * 
 * @author darcio
 */
@Component("alternativeResultBullet")
public class AlternativeComparator implements Comparator<CriterionResultVO>, Predicate {

	public AlternativeComparator() {
	}

	
	/**
	 * Predicate: Apenas para filtrar alternativas que nao foram encontradas particulas
	 */
	@Override
	public boolean evaluate(Object object) {
		CriterionResultVO altRes = (CriterionResultVO) object;
		
		AbstractCriterionVO abstractCriterionVO = altRes.getCriterionCoordinateVO().getAbstractCriterionVO();
		
		if(abstractCriterionVO instanceof AlternativeVO){
			
			ResultsTable rt = altRes.getTransientResultAnalysis();
			return rt.getCounter()>0;
			
		}else{
			return false;
		}
	}

	
	/**
	 * Ordena alternativas de acordo com os resultados de analise de particulas
	 */
	@Override
	public int compare(CriterionResultVO altRes1, CriterionResultVO altRes2) {
		
		ResultsTable rt1 = altRes1.getTransientResultAnalysis();
		ResultsTable rt2 = altRes2.getTransientResultAnalysis();
		
		//so para testar comparadores. TODO comentar em seguida
		/*
		comparaQuantidadeResultados(rt1,rt2);
		comparaCircularidade(rt1,rt2);
		comparaArea(rt1,rt2);
		comparaSolidez(rt1,rt2);
		*/
		
		int result = comparaQuantidadeResultados(rt1,rt2);
		
		if(!isDeterminant(result)){
			result = comparaCircularidade(rt1,rt2);
		}

		if(!isDeterminant(result)){
			result = comparaSolidez(rt1,rt2);
		}
		
		if(!isDeterminant(result)){
			result = comparaArea(rt1,rt2);
		}
		
		return result;
	}

	
	/**
	 * Primeiro criterio de ordenacao. Quantidade de resultados. TODO Predicate ja eliminaria isto, mas vou deixar por enquanto
	 * @param rt1
	 * @param rt2
	 * @return
	 */
	private int comparaQuantidadeResultados(ResultsTable rt1, ResultsTable rt2) {
		
		int ct1 = rt1.getCounter();
		int ct2 = rt2.getCounter();
		
		//Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
		if(ct1>0 ^ ct2>0){
			//caso uma das tabelas tenha resultado e a outra não 
			return ct1-ct2;
		}
				
		//se ambas tiverem resultados esta comparacao e indeterminada ate aqui
		return 0;
	}

	/**
	 * Pega maior area
	 * @param rt1
	 * @param rt2
	 * @return
	 */
	protected int comparaArea(ResultsTable rt1, ResultsTable rt2) {
		COLUMNS_PARTICLE atribute = COLUMNS_PARTICLE.AREA;
		return compareAttribute(rt1, rt2, atribute);
	}

	/**
	 * Pega maior solidez
	 * @param rt1
	 * @param rt2
	 * @return
	 */
	protected int comparaSolidez(ResultsTable rt1, ResultsTable rt2) {
		COLUMNS_PARTICLE atribute = COLUMNS_PARTICLE.SOLIDITY;
		return compareAttribute(rt1, rt2, atribute);
	}

	/**
	 * Pega maior circularidade
	 * @param rt1
	 * @param rt2
	 * @return
	 */
	protected int comparaCircularidade(ResultsTable rt1, ResultsTable rt2) {
		COLUMNS_PARTICLE atribute = COLUMNS_PARTICLE.CIRC;
		return compareAttribute(rt1, rt2, atribute);
	}

	/**
	 * Metodo utilitario, compara atributo informado
	 * @param rt1
	 * @param rt2
	 * @param atribute
	 * @return
	 */
	protected int compareAttribute(ResultsTable rt1, ResultsTable rt2, COLUMNS_PARTICLE atribute) {
		double c1 = getGreaterParticleValue(rt1, atribute);
		double c2 = getGreaterParticleValue(rt2, atribute);
		
		if(c1==c2){
			return 0; 

		}else if(c1>c2){
			return -1;
			
		}else {
			return 1;
			
		}
	}


	
	/**
	 * Recupera o valor solicitado da particula mais expressiva encontrada na analise
	 * @param rt
	 * @param column
	 * @return
	 */
	protected double getGreaterParticleValue(ResultsTable rt, COLUMNS_PARTICLE column) {
		
		int count = rt.getCounter();
		
		double area=0;
		double value=0;
		
		
		for (int i=0; i<count; i++){

			double iArea = getValue(rt, i, COLUMNS_PARTICLE.AREA);
			
			if(iArea>area){
				area=iArea;
				value = getValue(rt, i, column);
			}
			
		}
		
		return value;
	}


	
	/**
	 * recupera valor da tabela a partir de linha e columa
	 * @param rt
	 * @param circ
	 * @param i
	 * @param column
	 * @return
	 */
	protected double getValue(ResultsTable rt, int i, COLUMNS_PARTICLE column) {
		double value=0;
		if (rt.columnExists(column.col)) {
			value = rt.getValueAsDouble(column.col, i);
		}
		return value;
	}




	/**
	 * Checa se o resultado da comparacao e determinante
	 * @param result
	 * @return
	 */
	protected boolean isDeterminant(int result) {
		return result!=0;
	}

}
