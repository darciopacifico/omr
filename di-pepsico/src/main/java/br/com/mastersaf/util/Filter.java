package br.com.mastersaf.util;

/**
 * Stored the data for filtering objects
 * @author Macelo Muniz
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public class Filter {

  private String attribute;
  private Object value;
  private FilterOperator operation;

  
  /**
   * Return Name of attribute to be filter
   */
	public String getAttribute() {
		return attribute;
	}
	
	/**
	 * Set Name of attribute to be filter
	*/
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	/**
	 * Get the Operation to be perfom with the attribute 
	 */
	public FilterOperator getOperation() {
		return operation;
	}
	
	/**
	 * Set the Operation to be perfom with the attribute 
	 */
	public void setOperation(FilterOperator operation) {
		this.operation = operation;
	}
	
	/**
	 * Get the value of attribute to be filter
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Set the value of attribute to be filter
	 */
	public void setValue(Object value) {
		this.value = value;
	}
  
}