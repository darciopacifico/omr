package br.com.dlp.framework.math;

import java.math.BigInteger;

public class FactorialTeste {

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FactorialTeste factorialTeste = new FactorialTeste();
		
		System.out.println(factorialTeste.factorial(BigInteger.valueOf(999)));

		
		
		
	}
	
	public BigInteger factorial(BigInteger longFact){
		if(longFact.compareTo(BigInteger.ONE)<=0){
			return BigInteger.ONE; 
		}else{
			return longFact.multiply(  factorial(longFact.add( BigInteger.ONE.negate()))); 
		}
	}
	
	
}
