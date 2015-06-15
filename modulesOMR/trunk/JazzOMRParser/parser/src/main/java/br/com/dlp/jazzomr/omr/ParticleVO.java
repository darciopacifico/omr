/**
 * 
 */
package br.com.dlp.jazzomr.omr;




/**
 * Descricao de uma particula de referencia encontrada pelo imageJ
 * @author darcio
 *
 */
public class ParticleVO extends java.awt.geom.Point2D.Double {
	private static final long serialVersionUID = -4673570149325840751L;
	
	private java.lang.Double area;
	private java.lang.Double perimeter;
	private java.lang.Double solidity;
	private java.lang.Double circularity;
	
	private boolean angularReference=true;
	
	public ParticleVO() {
	}
	
	/**
	 * @return the area
	 */
	public java.lang.Double getArea() {
		return area;
	}
	/**
	 * @return the perimeter
	 */
	public java.lang.Double getPerimeter() {
		return perimeter;
	}
	/**
	 * @return the solidity
	 */
	public java.lang.Double getSolidity() {
		return solidity;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(java.lang.Double area) {
		this.area = area;
	}
	/**
	 * @param perimeter the perimeter to set
	 */
	public void setPerimeter(java.lang.Double perimeter) {
		this.perimeter = perimeter;
	}
	/**
	 * @param solidity the solidity to set
	 */
	public void setSolidity(java.lang.Double solidity) {
		this.solidity = solidity;
	}
	/**
	 * @return the circularity
	 */
	public java.lang.Double getCircularity() {
		return circularity;
	}
	/**
	 * @param circularity the circularity to set
	 */
	public void setCircularity(java.lang.Double circularity) {
		this.circularity = circularity;
	}
	
	
	
	public boolean isAngularReference() {
		return angularReference;
	}
	
	
	public void setAngularReference(boolean angularReference) {
		this.angularReference = angularReference;
	}
	
	
	/**
	 * @return the x
	 */
	@Override
	public double getX() {
		return super.x;
	}
	
	
	/**
	 * @return the y
	 */
	@Override
	public double getY() {
		return super.y;
	}
	
	
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		super.x = x;
	}
	
	
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		super.y = y;
	}
	
	
	
	/**
	 * Retorna o raio necessario para que a imagem da particula seja visualizada
	 * @return
	 */
	public java.lang.Double getVisibleRadius(){
		java.lang.Double raio = Math.sqrt(getArea() / Math.PI);
		return raio;
	}
	
}
