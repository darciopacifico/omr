/**
 * 
 */
package br.com.mastersaf.util;

/**
 * Class what configure sort by page
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public class Sort {
	private String nameProperty;
	private OrderOperator orderOperator;
	
	public Sort() {}
	
	public Sort(String nameProperty, OrderOperator orderOperator) {
		setNameProperty(nameProperty);
		setOrderOperator(orderOperator);
	}
	
	/**
	 * @return the nameProperty
	 */
	public String getNameProperty() {
		return nameProperty;
	}
	/**
	 * @param nameProperty the nameProperty to set
	 */
	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}
	/**
	 * @return the orderOperator
	 */
	public OrderOperator getOrderOperator() {
		return orderOperator;
	}
	/**
	 * @param orderOperator the orderOperator to set
	 */
	public void setOrderOperator(OrderOperator orderOperator) {
		this.orderOperator = orderOperator;
	}
}
