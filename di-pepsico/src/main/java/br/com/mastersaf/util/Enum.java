package br.com.mastersaf.util;

/**
 * Template class for a Enumeration implemantation
 * @author Macelo Muniz
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
abstract public class Enum {
	
  protected String state;


	public Enum(){
		
	}
	
	public Enum(String state){
		this.state = state;
	}

	public boolean equals(Object obj) {
		boolean check = false;
		if(obj instanceof Enum){
			Enum operator = (Enum)obj;
			check = operator.getValue().equals(this.state); 
		}
		return check;
	}
	
	/**
	 * Return the value of state of enumaration
	 */
	public String getValue(){
		return this.state;
	}

}