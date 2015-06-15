package br.com.jazz.codegen.annotation;

public class Fib {
	
	public static void main(String[] args) {
		Fib.dlpFib(44, 0, 1);
	}
	
	public static long dlpFib(int i, long a, long b){
		System.out.println(a);
		  
		return i==0?a:Fib.dlpFib(--i,a+b, a);
		
	}
	
}