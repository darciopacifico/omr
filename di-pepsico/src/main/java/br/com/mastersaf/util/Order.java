package br.com.mastersaf.util;

/**
 * Store the data for sort a selection
 * @author Macelo Muniz
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public class Order {

	private String attribute;

	private OrderOperator operation;

	/**
	 * Get the name of the attribute to be sort
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Set the name of the attribute to be sort
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Get the ascending of the sort to be perfrom
	 */
	public OrderOperator getOperation() {
		return operation;
	}

	/**
	 * Set the ascending of the sort to be perfrom
	 */
	public void setOperation(OrderOperator operation) {
		this.operation = operation;
	}

}