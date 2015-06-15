/**
 * 
 */
package br.com.dlp.framework.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementação abstrata de DAOs para JDBC, utilizando Spring JdbcDaoSupport
 * 
 * @author dpacifico
 * @param <P>
 * @param <P>
 * @param <B>
 * 
 */
public abstract class AbstractJDBCDAO<B extends IBaseVO<? extends Serializable>> extends JdbcDaoSupport implements IDAO<B> {
	
	private static final long serialVersionUID = 5730970152568135815L;
	
	public AbstractJDBCDAO() {
	}
	
	/**
	 * Retorna a classe referente ao primeiro parameter type definido na implementação de AbstractHibernateDAO
	 * 
	 * @return
	 */
	protected Class getVoClass() {
		final Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		
		return (Class) types[0];
	}
	
}
