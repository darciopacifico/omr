/**
 * 
 */
package br.com.dlp.framework.dao;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;

/**
 * Especializacao de detached criteria para realizacao de escolha de fetchprofile
 * @author darcio
 *
 */
public class JazzDetachedCriteria extends DetachedCriteria {

	private static final long serialVersionUID = -6111384083164351821L;

	private Set<String> fetchProfiles=new HashSet<String>(2);

	public JazzDetachedCriteria(String entityName) {
		super(entityName);
	}

	
	/**
	 * Habilita fetch profiles informados
	 * @param fetchProfileNames
	 */
	public void enableFetchProfile(String... fetchProfileNames) {
		
		if(fetchProfileNames==null){
			this.clearFetchProfile();
			return;
		}
		
		for (String fetchProfileName : fetchProfileNames) {
			if(StringUtils.isNotBlank(fetchProfileName)){
				this.fetchProfiles.add(fetchProfileName);
			}
		}
		
	}

	
	/**
	 * Desabilita fetch profiles informados
	 * @param fetchProfileNames
	 */
	public void disableFetchProfile(String... fetchProfileNames) {
		
		if(fetchProfileNames==null){
			return;
		}
		
		for (String fetchProfileName : fetchProfileNames) {
			this.fetchProfiles.remove(fetchProfileName);
		}
		
	}

	public void clearFetchProfile() {
		this.fetchProfiles.clear();
	}
	
	/**
	 * @param session
	 */
	protected void enableFetchProfiles(Session session) {
		for (String fetchProfileName : this.fetchProfiles) {
			session.enableFetchProfile(fetchProfileName);
		}
	}
	

	/**
	 * Sobrescrita de getExecutableCriteria apenas para habilitar fetchProfile 
	 */
	@Override
	public Criteria getExecutableCriteria(Session session) {

		enableFetchProfiles(session);
		
		Criteria executableCriteria = super.getExecutableCriteria(session);
		return executableCriteria;
	}	
	
	
	public JazzDetachedCriteria(String entityName, String alias) {
		super(entityName, alias);
	}

	public JazzDetachedCriteria(CriteriaImpl impl, Criteria criteria) {
		super(impl, criteria);
	}

	public static JazzDetachedCriteria forEntityName(String entityName) {
		return new JazzDetachedCriteria(entityName);
	}
	
	public static JazzDetachedCriteria forEntityName(String entityName, String alias) {
		return new JazzDetachedCriteria(entityName, alias);
	}
	
	public static JazzDetachedCriteria forClass(Class clazz) {
		return new JazzDetachedCriteria( clazz.getName() );
	}
	
	public static JazzDetachedCriteria forClass(Class clazz, String alias) {
		return new JazzDetachedCriteria( clazz.getName() , alias );
	}

}
