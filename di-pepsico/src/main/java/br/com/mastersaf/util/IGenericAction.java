/**
 * 
 */
package br.com.mastersaf.util;

import java.io.Serializable;

/**
 * Integer what contains global forward's
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public interface IGenericAction extends Serializable {
	public static final String RESULT = "result";
	public static final String AUTO_COMPLETE = "autoComplete";
	public Bean getBean();
	public void setBean(Bean bean);
	public Serializable getId();
}