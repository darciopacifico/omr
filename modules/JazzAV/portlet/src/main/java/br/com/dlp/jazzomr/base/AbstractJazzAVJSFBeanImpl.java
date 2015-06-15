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

import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.framework.jsf.IJazzDataProvider;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzav.person.PessoaBusiness;
import br.com.dlp.jazzav.person.PessoaVO;

/**
 * Implementacao abstraca de AbstractJSFBeanImpl, contendo funcionalidades para segurança e frame de tela (login, logoff, saudações, menu etc)
 **/
public abstract class AbstractJazzAVJSFBeanImpl<B extends IBaseVO<? extends Serializable>> extends AbstractJSFBeanImpl<B> implements IJazzDataProvider<B> {
	
	private static final long serialVersionUID = 2356475644225235642l;

	@Autowired
	private PessoaBusiness pessoaBusiness;
	/*
	private EmpresaVO empresaUsuarioLogado;
	*/
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
	

	
	
	/**
	 * Recupera a empresa do usuario logado
	 * @return
	public EmpresaVO getEmpresaUsuarioLogado(){
		
		if(this.empresaUsuarioLogado==null){
			AbstractJazzOMRBusinessImpl<B> businessImpl = (AbstractJazzOMRBusinessImpl<B>) getBusiness();
			this.empresaUsuarioLogado = businessImpl.getEmpresaUsuarioLogado();
		}
		
		return empresaUsuarioLogado;
	}
	 */

	
	public PessoaVO getPessoaVO() {
		
		if(pessoaVO==null || (pessoaVO.getPK()!=null && !pessoaVO.getPK().equals(getPrincipal().getName()))){
			PessoaVO pessoaVO = pessoaBusiness.findPessoa(getPrincipal());
			setPessoaVO(pessoaVO);
		}
		
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}


	
}