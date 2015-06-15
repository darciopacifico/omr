/**
 * 
 */
package br.com.dlp.framework.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Classe helper para pesquisa sobre segurança. Pode ser utilizada em qualquer contexto. Trabalha com springSecurity
 * 
 * @author darcio
 */
public class SecurityHelper {
	
	private static SecurityHelper securityHelper;
	
	private SecurityHelper(){}

	/**
	 * Singleton method
	 * @return
	 */
	public static SecurityHelper getInstance(){
		if(securityHelper==null){
			securityHelper = new SecurityHelper();
		}
		return securityHelper;
	}
	

	/**
	 * Testa se algum dos roles informados está presente nas authorities do usuários logado no momento
	 * @param roles
	 * @return
	 */
  public boolean ifAnyGranted(List<String> roles){
  	
   	Collection<String> authorities = getAuthorities();
   	
   	for (String auth : authorities) {
   		for (String role : roles) {
				
   			if(auth.equals(role)){
   				return true;
   			}
   			
			}
		}
   	return false;
  }
  
  
	
	/**
	 * Recupera o login name, priorizando o principal do spring. Caso não encontre pega o loginName do principal informado.
	 * @param principal
	 * @return
	 */
	public String getLoginName(Principal principal) {
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
	public Collection<String> getAuthorities() {
		Object springPrincipal = getSpringPrincipal();

		Collection<String> authorities = null;
		
		if(springPrincipal!=null && springPrincipal instanceof UserDetails) {

			UserDetails userDetails = (UserDetails)springPrincipal;
			
			 Collection<? extends GrantedAuthority> springAuthorities = userDetails.getAuthorities();
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
	public String getLoginName() {
		return getLoginName(null);
	}

	/**
	 * Recupera o principal do SpringSecurity
	 * @return
	 */
	public Object getSpringPrincipal() {
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

	
	
}
