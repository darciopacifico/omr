/**
 * 
 */
package br.com.dlp.jazzomr.base;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.ILoggableVO;

/**
 * @author darcio
 *
 */
public class JazzOMRDetachedCriteria extends JazzDetachedCriteria {

	private static final long serialVersionUID = 7791511952915334885L;

	private Class voClass;
	
	
	/**
	 * @param entityName 
	 */
	public JazzOMRDetachedCriteria(Class clazz) {
		super(clazz.getName());
		this.voClass = clazz;
	}

	/**
	 * @param entityName
	 * @param alias
	 */
	public JazzOMRDetachedCriteria(Class clazz, String alias) {
		super(clazz.getName(), alias);
		this.voClass = clazz;
	}

	/**
	 * @param impl
	 * @param criteria
	 */
	public JazzOMRDetachedCriteria(CriteriaImpl impl, Criteria criteria) {
		super(impl, criteria);
	}

	/**
	 * Sobrescrita de getExecutableCriteria apenas para habilitar fetchProfile 
	 */
	@Override
	public Criteria getExecutableCriteria(Session session) {
		//super super.getExecutableCriteria(session) ja pode acionar os fetchProfiles
		Criteria executableCriteria = super.getExecutableCriteria(session);
		
		//addSameGroupRestriction(executableCriteria);
		addSameOrgRestriction(executableCriteria);
		
		return executableCriteria;
	}	
	
	
	/**
	 * Adiciona uma restrição de que pelo menos um grupo do usuário logado seja comum aos grupos do usuario criador da entidade
	 * @param executableCriteria 
	 * 
	 * @param criteria
	 */
	protected void addSameGroupRestriction(Criteria executableCriteria) {
		if(isLoggable()){
			
			Collection<String> authorities = getAuthorities();
			StringBuffer strItens  = new StringBuffer("'dfdef344'");
			for (String authority : authorities) {
				strItens.append(", '").append(authority).append("' ");
			}
			
			String groupRetriction = 
			" select distinct(pk) from tb_pessoa pes "+
			" inner join tj_grupovo_pessoas gp 	on gp.fk_pessoas_pessoa = pes.pk "+
			" inner join tb_grupo gru 			on gru.pk = gp.fk_grupo "+
			" where gru.description in ("+strItens+") ";

			executableCriteria.add(Restrictions.sqlRestriction(" {alias}.criado_por in ("+groupRetriction+")  "));
			
		}
	}

	

	/**
	 * Recupera o login name, priorizando o principal do spring. Caso não encontre pega o loginName do principal informado.
	 * @param principal
	 * @return
	 */
	protected Collection<String> getAuthorities() {
		Object springPrincipal = getSpringPrincipal();

		Collection<String> authorities = null;
		
		if(springPrincipal!=null && springPrincipal instanceof UserDetails) {

			UserDetails userDetails = (UserDetails)springPrincipal;
			
			Collection<GrantedAuthority> springAuthorities = userDetails.getAuthorities();
			authorities = new ArrayList<String>(springAuthorities.size());
			
			for (GrantedAuthority grantedAuthority : springAuthorities) {
				authorities.add(grantedAuthority.getAuthority());
			}
				
		}else{
			authorities = new ArrayList<String>(0);
		}
		
		return authorities;
	}


	/**
	 * Recupera o login name, priorizando o principal do spring. Caso não encontre pega o loginName do principal informado.
	 * @param principal
	 * @return
	 */
	protected String getLoginName(Principal principal) {
		Object principal2 = getSpringPrincipal();
	
		String loginName=""; 

		if(principal2!=null){
			
			if (principal2 instanceof UserDetails) {
				loginName = ((UserDetails)principal2).getUsername();
			} else {
				loginName = principal2.toString();
			}
			
		}else if (principal != null) {
			loginName = principal.getName();
		}else{
			return null;
		}
		return loginName;
	}
	
	

	

	/**
	 * Recupera o login name, priorizando o principal do spring. Caso não encontre pega o loginName do principal informado.
	 * @param principal
	 * @return
	 */
	protected String getLoginName() {
		return getLoginName(null);
	}

	/**
	 * Recupera o principal do SpringSecurity
	 * @return
	 */
	protected Object getSpringPrincipal() {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context==null){
			return null;
		}		
		
		Authentication authentication = context.getAuthentication();
		if(authentication==null){
			return null;
		}
		
		Object principal = authentication.getPrincipal();
		return principal;
	}
	
	/**
	 * Caso a entidade corrente seja uma informação que não deva ser visualizada por outras empresas (IBelongsOrganizationVO), 
	 * adiciona uma restrição ao criterio de pesquisa onde apenas dados da mesma empresa do usuario logado devam ser retornados.
	 * 
	 * @param criteria
	 */
	protected void addSameOrgRestriction(Criteria criteria) {
		if(isOrganizationRestrict()){
			String loginName = getLoginName();
			
			if(loginName==null){
				throw new JazzRuntimeException("Usuario logado inválida (null). A entidade '"+getVoClass().getName()+"' obrigatoriamente deve ser restringida por empresa.");
			}
			
			criteria.add(Restrictions.sqlRestriction(" {alias}.fk_empresa = (select max(fk_empresa) from tb_pessoa where login = ?)  ",loginName,Hibernate.STRING));
		}
	}


	/**
	 * É passível de logging
	 * @return
	 */
	protected boolean isLoggable() {
		return ILoggableVO.class.isAssignableFrom(getVoClass());
	}
	
	/**
	 * @return
	 */
	protected boolean isOrganizationRestrict() {
		return IOrganizationRestrictVO.class.isAssignableFrom(getVoClass());
	}

	public static JazzOMRDetachedCriteria forClass(Class clazz) {
		return new JazzOMRDetachedCriteria( clazz );
	}
	
	public static JazzOMRDetachedCriteria forClass(Class clazz, String alias) {
		return new JazzOMRDetachedCriteria( clazz , alias );
	}
	
	public Class getVoClass() {
		return voClass;
	}
	
}
