package br.com.dlp.jazzomr.exam.coordinate;

public interface IOMRMarkable {

	public Object getPK();
	
	/**
	 * @return the pagina
	 */
	public Integer getPagina();

	/**
	 * @return the x
	 */
	public Integer getX();

	/**
	 * @return the y
	 */
	public Integer getY();

	/**
	 * @return the h
	 */
	public Integer getH();

	/**
	 * @return the w
	 */
	public Integer getW();

	/**
	 * @param pagina the pagina to set
	 */
	public void setPagina(Integer pagina);

	/**
	 * @param x the x to set
	 */
	public void setX(Integer x);

	/**
	 * @param y the y to set
	 */
	public void setY(Integer y);

	/**
	 * @param h the h to set
	 */
	public void setH(Integer h);

	/**
	 * @param w the w to set
	 */
	public void setW(Integer w);

}