/**
 * 
 */
package br.com.dlp.jazzomr.poc;

/**
 * Recorte da regiao da imagem que contem a identificacado do documento
 * @author t_dpacifico
 */
public class IdentityRegion {
	
	private double xrel, yrel, wrel, hrel;
	
	/**
	 * @return the xperc
	 */
	public double getXrel() {
		return xrel;
	}
	
	/**
	 * @return the yperc
	 */
	public double getYrel() {
		return yrel;
	}
	
	/**
	 * @return the wperc
	 */
	public double getWrel() {
		return wrel;
	}
	
	/**
	 * @return the hperc
	 */
	public double getHrel() {
		return hrel;
	}
	
	/**
	 * @param xperc the xperc to set
	 */
	public void setXrel(double xperc) {
		xrel = xperc;
	}
	
	/**
	 * @param yperc the yperc to set
	 */
	public void setYrel(double yperc) {
		yrel = yperc;
	}
	
	/**
	 * @param wperc the wperc to set
	 */
	public void setWrel(double wperc) {
		wrel = wperc;
	}
	
	/**
	 * @param xperc
	 * @param yperc
	 * @param wperc
	 * @param hperc
	 */
	public IdentityRegion(double xperc, double yperc, double wperc, double hperc) {
		super();
		xrel = xperc;
		yrel = yperc;
		wrel = wperc;
		hrel = hperc;
	}
	
	/**
	 * @param hperc the hperc to set
	 */
	public void setHrel(double hperc) {
		hrel = hperc;
	}
	
	
	
	
}
