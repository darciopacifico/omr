package br.com.mastersaf.util;

/**
 * Stored the data to page a list of objets returned
 * @author Macelo Muniz
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public class Page {

	private Long maxItens;

	private Long pageNumber;

	/**
	 * Get the max objects per page to be displayed
	 */
	public Long getMaxItens() {
		return maxItens;
	}

	/**
	 * Set the max objects per page to be displayed
	 */
	public void setMaxItens(Long maxItens) {
		this.maxItens = maxItens;
	}

	/**
	 * Get the number of the page to be displayed
	 */
	public Long getPageNumber() {
		return pageNumber;
	}

	/**
	 * Set the number of the page to be displayed
	 */
	public void setPageNumber(Long number) {
		this.pageNumber = number;
	}
	
	/**
	 * Get the first item of the actual page
	 * @return
	 */
	public Long getFirstItem() {
		Long first = new Long(0);
		if (pageNumber != null && pageNumber >0)
			first = pageNumber * maxItens;
		return first;
	}

}