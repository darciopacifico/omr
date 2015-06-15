/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.jazzav.StatusEnum;
import br.com.dlp.jazzav.exam.IJazzOMRBusiness;
import br.com.dlp.jazzav.person.EAuthority;
import br.com.dlp.jazzav.person.GrupoBusiness;
import br.com.dlp.jazzav.person.GrupoVO;
import br.com.dlp.jazzav.person.PessoaBusiness;
import br.com.dlp.jazzav.person.PessoaVO;
import br.com.dlp.jazzav.person.VGroupUsuario;
import br.com.dlp.jazzomr.base.AbstractJazzOMRJSFBeanImpl;

/**
 * Incluir arquivo com comentários
 * 
 * Implementação de Bean JSF para o componente PessoaVO
 * 
 **/
@Scope(value = "session")
@Component
public class PessoaJSFBean extends AbstractJazzOMRJSFBeanImpl<PessoaVO> {

	private static final long serialVersionUID = 2195241915100521959L;

	@Autowired
	private PessoaBusiness pessoaBusiness;

	@Autowired
	private GrupoBusiness grupoBusiness;

	private List<GrupoVO> freeGrupos;

	/*
	private List<EmpresaVO> empresaVOs;
	 */
	
	protected ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */

	private String login;
	private String email;
	private String nome;
	private String telefone;
	private Date dtIncFrom;
	private Date dtIncTo;
	private Date dtAltFrom;
	private Date dtAltTo;
	private StatusEnum status;

	
	/**
	 * Revoga acesso ao sistema.
	 * @return
	 */
	public String actionRevogarAcesso(){
		
		PessoaVO pessoaVO = getTmpVO();

		try{
			PessoaVO pessoaRevoked = pessoaBusiness.revogarAcesso(pessoaVO);
			setTmpVO(pessoaRevoked);

		}catch( JazzBusinessException e){
			parseBusinessException(e);
			
		}
		
		return "";
	}
	
	@Override
	public Set<String> getValidationGroupsSubModules() {
		Set<String> validationGroups = super.getValidationGroups();
		validationGroups.add("br.com.dlp.jazzav.person.VGroupUsuario");
		return validationGroups;
	}
	
	
	public List<? extends Enum> autorizeAnyOf(){
		ArrayList<EAuthority> authorities = new ArrayList<EAuthority>();
		authorities.add(EAuthority.ADMIN);
		return authorities;
	}

	/**
	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar...
	 * 
	 * @return
	 */
	public List<SelectItem> getStatusList() {
		return getEnums(StatusEnum.class);
	}

	
	@Override
	public String actionNovo() {
		String actionNovo = super.actionNovo();
		
		getTmpVO().setNovo(true);
		
		return actionNovo;
	}
	
	@Override
	public String actionSalvar() throws JazzBusinessException {
		PessoaVO pessoaVO = getTmpVO();
		
		if(pessoaVO==null){
			return "";
		} 
		
		
		if( StringUtils.isNotBlank(pessoaVO.getEmail())){

			if(validatePessoa(pessoaVO)){
				return super.actionSalvar();	
			}else{
				return "";
			}
		
		}else{
			return super.actionSalvar();
		}
		
		
		

	}

 	

	/**
	 * Valida vo de pessoa informado
	 * @param pessoaVO2
	 */
	public boolean validatePessoa(PessoaVO pessoaVO2) {
		
		boolean isValid = true;
		
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<PessoaVO>> constraints = validator.validate(pessoaVO2, Default.class,VGroupUsuario.class);
		
		if(constraints==null){
			FacesContext context = FacesContext.getCurrentInstance();					
			for (ConstraintViolation<PessoaVO> constraintViolation : constraints) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, constraintViolation.getMessage(), constraintViolation.getMessage()));
				isValid=false;
			}
		}

		return isValid;
	}
	

	

	
	
	@Override
	public void setTmpVO(PessoaVO tmpVO) {
		if (tmpVO != null && tmpVO.getPK() != null) {
			IJazzOMRBusiness<PessoaVO> business = (IJazzOMRBusiness<PessoaVO>) getBusiness();
			tmpVO = business.findByBeanPK(tmpVO);
		}

		/*
		if (tmpVO != null && ifAnyGranted("MASTER")) {
			List<EmpresaVO> empresaVOs = empresaBusiness.findAll();
			setEmpresaVOs(empresaVOs);
		}
		*/

		super.setTmpVO(tmpVO);
	}
	
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo. Pesquisa e retorna lista de PessoaVO com paginação e
	 * ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<PessoaVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		List<PessoaVO> resultados = pessoaBusiness.findPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
		return resultados;
	}

	/**
	 * Implementação de contagem de registros específica para este módulo. Este valor será cacheado até que o método invalidateRowCountCache() seja
	 * acionado. Na solução de design proposta o cache de rowCount é válido até que os argumentos de pesquisa sejam modificados.
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl#rowCount()
	 */
	@Override
	protected Long rowCount() {
		return pessoaBusiness.countPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}


	
	
	/**
	 * Accessor Method
	 * 
	 * @return email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Accessor Method
	 * 
	 * @return nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Accessor Method
	 * 
	 * @return telefone
	 */
	public String getTelefone() {
		return this.telefone;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtIncFrom
	 */
	public Date getDtIncFrom() {
		return this.dtIncFrom;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtIncTo
	 */
	public Date getDtIncTo() {
		return this.dtIncTo;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtAltFrom
	 */
	public Date getDtAltFrom() {
		return this.dtAltFrom;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtAltTo
	 */
	public Date getDtAltTo() {
		return this.dtAltTo;
	}

	/**
	 * Accessor Method
	 * 
	 * @return status
	 */
	public StatusEnum getStatus() {
		return this.status;
	}


	/**
	 * Mutator Method
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Mutator Method
	 * 
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Mutator Method
	 * 
	 * @param telefone
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtIncFrom
	 */
	public void setDtIncFrom(Date dtIncFrom) {
		this.dtIncFrom = dtIncFrom;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtIncTo
	 */
	public void setDtIncTo(Date dtIncTo) {
		this.dtIncTo = dtIncTo;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtAltFrom
	 */
	public void setDtAltFrom(Date dtAltFrom) {
		this.dtAltFrom = dtAltFrom;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtAltTo
	 */
	public void setDtAltTo(Date dtAltTo) {
		this.dtAltTo = dtAltTo;
	}

	/**
	 * Mutator Method
	 * 
	 * @param status
	 */
	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[] { "login", "email", "nome", "telefone", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<PessoaVO> getBusiness() {
		return this.pessoaBusiness;
	}

	/**
	 * @return the freeGrupos
	 */
	public List<GrupoVO> getFreeGrupos() {
		return freeGrupos;
	}

	/**
	 * @param freeGrupos
	 *          the freeGrupos to set
	 */
	public void setFreeGrupos(List<GrupoVO> freeGrupos) {
		this.freeGrupos = freeGrupos;
	}

	/*
	public List<SelectItem> getEmpresaVOs() {
		if (empresaVOs == null) {
			return new ArrayList<SelectItem>(0);
		}

		List<SelectItem> selectItems = new ArrayList<SelectItem>(empresaVOs.size());

		for (EmpresaVO empresaVO : empresaVOs) {
			selectItems.add(new SelectItem(empresaVO, empresaVO.getNome(), "desc:" + empresaVO.getNome()));
		}

		return selectItems;
	}

	public void setEmpresaVOs(List<EmpresaVO> empresaVOs) {
		this.empresaVOs = empresaVOs;
	}
	*/


	public void setAuts(List<EAuthority> auts) {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}