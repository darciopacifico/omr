package br.com.dlp.jazzomr.selfreg;

import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.jazzav.endereco.LogradouroVO;
import br.com.dlp.jazzav.person.EAuthority;
import br.com.dlp.jazzav.person.EmailNotFoundException;
import br.com.dlp.jazzav.person.InvalidResetTokenException;
import br.com.dlp.jazzav.person.NotWaitingForResetException;
import br.com.dlp.jazzav.person.PessoaAlreadyExistException;
import br.com.dlp.jazzav.person.PessoaBusiness;
import br.com.dlp.jazzav.person.PessoaVO;

/**
 * 
 * @author darcio
 * 
 */
@ManagedBean
@SessionScoped
@Scope(value = "session")
@Component
public class SelfRegBean extends AbstractJSFBeanImpl<PessoaVO> {

	private static final long serialVersionUID = -3150782260624274568L;

	private PessoaVO pessoaVO = new PessoaVO();

	@Autowired
	private PessoaBusiness pessoaBusiness;

	private Boolean mostrarRedefinirSenha=false;
	
	@Autowired
	@Qualifier("jazzAuthManager")
	protected AuthenticationManager authenticationManager;

	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}


	
	
	/**
	 * Reseta senha do cliente
	 */
	public void resetSenha() {

		FacesContext context = FacesContext.getCurrentInstance();

		// valida email e retorna colecao de criticas, caso haja alguma
		Set<ConstraintViolation<PessoaVO>> constraints = validaEmailPessoa();

		// nenhum erro no email
		if (constraints!=null) {

			try {

				// envia email resetando senha
				pessoaBusiness.resetSenha(pessoaVO);

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Um email de recuperação de senha foi enviado para " + pessoaVO.getEmail() + "!", ""));

			} catch (EmailNotFoundException e) {
				log.warn("Email não encontrado em nossas base de clientes! " + pessoaVO, e);

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Email não encontrado em nossas base de clientes!",
						"Email não encontrado em nossas base de clientes!"));
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor, registre-se!", "Por favor, registre-se!"));

			} catch (JazzBusinessException e) {
				log.error("Não foi possível enviar email de recuperação de senha. Tente novamente mais tarde. " + pessoaVO, e);

				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao tentar enviar email de recuperacao de senha!", ""));
			}
		}

	}

	/**
	 * 
	 * @return
	 */
	public String confirmReset() {
		FacesContext context = FacesContext.getCurrentInstance();
		String flow = "resetConfirm";

		try {
			this.pessoaVO = pessoaBusiness.confirmResetPessoa(pessoaVO);

		} catch (EmailNotFoundException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Email não encontrado!", ""));
			flow = null;

		} catch (InvalidResetTokenException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Token inválido!", ""));
			flow = null;

		} catch (NotWaitingForResetException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Redefinição de senha não esperado!", ""));
			flow = null;
			
		} catch (JazzBusinessException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro desconhecido ao tentar redefinir senha. Tente novamente mais tarde! ("+e.getMessage()+")!", ""));
			flow = null;
			
		}

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha redefinida com sucesso!", ""));

		// realiza autenticação automatica
		autenticaUsuario(pessoaVO);

		// anuncio
		return flow;
	}

	/**
	 * @return
	 */
	protected Set<ConstraintViolation<PessoaVO>> validaEmailPessoa() {
		FacesContext context2 = FacesContext.getCurrentInstance();

		Validator validator = validatorFactory.getValidator();

		Set<ConstraintViolation<PessoaVO>> constraints = validator.validateProperty(pessoaVO, "email");

		if ((constraints)!=null) {
			for (ConstraintViolation<PessoaVO> constraintViolation : constraints) {
				context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, constraintViolation.getMessage(), constraintViolation.getMessage()));
			}
		}
		return constraints;
	}


	
		
	/**
	 * Registra a pessoa e já efetua login
	 * 
	 * @return
	 */
	public String salvarPessoa() {

		String returnString = null;

		try {

			boolean valid = validateVO(this.pessoaVO);

			pessoaVO.getAuthorities().add(EAuthority.COMPRADOR);

			if (valid) {
				pessoaBusiness.saveOrUpdate(this.pessoaVO);
				returnString = "checkout";

				autenticaUsuario(pessoaVO);

			}

		} catch (PessoaAlreadyExistException e) {

			log.error("Já existe um usuário registrado com este email!", e);

			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage("txtEmail", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Já existe um usuário registrado com este email! ",""));
			context.addMessage("txtEmail", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Para redefinir sua senha clique em 'Redefinir Senha'! ",""));
			
			
			setMostrarRedefinirSenha(true);

		} catch (JazzBusinessException e) {

			log.error("Erro ao tentar registrar usuario!", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar usuario!");

		} catch (ConstraintViolationException e) {

			log.error("Erro ao tentar registrar usuario!", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar usuario!");

		} catch (Exception e) {
	
			log.error("Erro ao tentar registrar usuario!", e);
			addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar registrar usuario!");
	
		}

		return returnString;

	}

	/**
	 * Autentica o usuário recem criado
	 * 
	 * @param pessoaVO2
	 */
	protected void autenticaUsuario(PessoaVO pessoaVO2) {

		autenticaUsuario(pessoaVO2.getEmail(), pessoaVO2.getSenha());

	}

	/**
	 * Autentica o usuário recem criado
	 * 
	 * @param usuario
	 * @param senha
	 */
	protected void autenticaUsuario(String usuario, String senha) {
		// forca autenticacao
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario, senha);

		token.setDetails(new WebAuthenticationDetails(request));

		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}


	@Override
	public List<? extends Enum> autorizeAnyOf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actionPesquisar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCamposLimparPesquisa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IBusiness<PessoaVO> getBusiness() {
		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * MOstrar redefinir senha
	 * @return
	 */
	public Boolean getMostrarRedefinirSenha() {
		
		return mostrarRedefinirSenha;
		
	}

	public void setMostrarRedefinirSenha(Boolean mostrarRedefinirSenha) {
		this.mostrarRedefinirSenha = mostrarRedefinirSenha;
	}


}
