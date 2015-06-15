/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.base;

import java.io.Serializable;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl;
import br.com.dlp.framework.jsf.IJazzDataProvider;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.person.PessoaBusiness;
import br.com.dlp.jazzomr.person.PessoaVO;

/**
 * Implementacao abstraca de AbstractPaginatedJSFBeanImpl, contendo funcionalidades para segurança e frame de tela (login, logoff, saudações, menu etc)
 **/
public abstract class AbstractJazzOMRJSFBeanImpl<B extends IBaseVO<? extends Serializable>> extends AbstractPaginatedJSFBeanImpl<B> implements IJazzDataProvider<B> {
	
	private static final long serialVersionUID = 2356475644225235642l;

	@Autowired
	private PessoaBusiness pessoaBusiness;

	private PessoaVO pessoaVO;

	
	public String logoff(){

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext(); 
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse(); 
		HttpSession session = (HttpSession) externalContext.getSession(false); 
		session.invalidate(); 
		FacesContext context = FacesContext.getCurrentInstance(); 
		Application application = context.getApplication(); 
		application.getNavigationHandler().handleNavigation(context, "/index.faces", "index"); 
		return null; 
	}
	
	public PessoaVO getPessoaVO() {
		
		if(pessoaVO==null || (pessoaVO.getLogin()!=null && !pessoaVO.getLogin().equals(getPrincipal().getName()))){
			PessoaVO pessoaVO = pessoaBusiness.findPessoa(getPrincipal());
			setPessoaVO(pessoaVO);
		}
		
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}
	
	
}