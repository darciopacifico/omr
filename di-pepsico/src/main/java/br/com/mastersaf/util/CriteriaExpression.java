package br.com.mastersaf.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Data for realize a object selection from various attributes, like filtering
 * and page
 * @author Macelo Muniz
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
*/
public class CriteriaExpression {

	private List<Order> orders;

	private Collection<Filter> filters;

	private Page page;

	private String objectName;
	
	public CriteriaExpression(Class<? extends Bean> clazzBean) {
		this.objectName = clazzBean.getName();
	}
	
	/**
	 * Return Full name of the class of the object to be filter
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * Set Full name of the class of the object to be filter
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * Add a filter for selection
	 */
	public void addFilter(Filter filter) {
		if(filters==null)
			filters = new HashSet<Filter>();
		
		filters.add(filter);
	}
	
	/**
	 * Add a map of filter for selection, the key need be the attribute name
	 * @param mapFilter
	 */
	@SuppressWarnings("unchecked")
	public void addFilters(Map mapFilter, boolean isFilter) {
		if(filters == null){
			filters = new HashSet<Filter>();
		}
		if(mapFilter != null){
			String key = null;
			Filter filter = null;
			Object object = null;
			for(Iterator i = mapFilter.keySet().iterator(); i.hasNext();){
				key = (String) i.next();
				object = mapFilter.get(key);
				if(object != null){
					// If number equal 0 dont's none.
					if(object instanceof Number){
						Number numberObject = (Number) object;
						if(numberObject instanceof BigDecimal){
							if(((BigDecimal) object).doubleValue() <= 0){
								continue;
							}
						} else {
							if(numberObject.intValue() <= 0){
								continue;
							}
						}
					}
					filter = new Filter();
					filter.setAttribute(key);
					filter.setValue(object);
					if(isFilter){
						filter.setOperation((object instanceof String ? FilterOperator.CONTAINS : FilterOperator.EQUAL));
					} else {
						filter.setOperation(FilterOperator.EQUAL);
					}
					addFilter(filter);
				}
			}
		}
	}

	/**
	 * Add a order for sorting
	 */
	public void addOrder(Order order) {
		if(orders==null)
			orders = new ArrayList<Order>();
		
		orders.add(order);
	}

	/**
	 * Return the page to be displayed
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Set the page the be displayed
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * Return all the filters to be executed
	 */
	public Collection<Filter> getFilters() {
		return filters;
	}

	/**
	 * Return all the orders to be executed s
	 */
	public List<Order> getOrders() {
		return orders;
	}

}