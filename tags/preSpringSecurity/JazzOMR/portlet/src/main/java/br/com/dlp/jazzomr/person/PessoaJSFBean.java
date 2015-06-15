/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRJSFBeanImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.IJazzOMRBusiness;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente PessoaVO
 *
 **/
@Scope(value="session")
@Component
public class PessoaJSFBean extends AbstractJazzOMRJSFBeanImpl<PessoaVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private PessoaBusiness pessoaBusiness;

 	@Autowired
 	private GrupoBusiness grupoBusiness;

 	private List<GrupoVO> freeGrupos;

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
	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar...
	 * @return
	 */
	public List<SelectItem> getStatusList() {
		return getEnums(StatusEnum.class);
	}

 	@Override
 	public void setTmpVO(PessoaVO tmpVO) {
 		
 		if(tmpVO!=null && tmpVO.getPK()!=null){
 			IJazzOMRBusiness<PessoaVO> business = (IJazzOMRBusiness) getBusiness();
 			tmpVO = business.findByBeanPK(tmpVO);
 		}
 		
 		
 		super.setTmpVO(tmpVO);
 	}


	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo.
	 * Pesquisa e retorna lista de PessoaVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<PessoaVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
	
		List<PessoaVO> resultados = pessoaBusiness.findPessoaVO(
login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);		
		return resultados;
	}
		
	/**
	 * Implementação de contagem de registros específica para este módulo.
	 * Este valor será cacheado até que o método invalidateRowCountCache() seja acionado.
	 * Na solução de design proposta o cache de rowCount é válido até que os argumentos de pesquisa sejam modificados.
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl#rowCount()
	 */
	@Override
	protected Long rowCount() {
		return pessoaBusiness.countPessoaVO(
login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);	}
	

	/**
	 * Accessor Method
 	 * @return login
 	 */
	public String getLogin(){
		return this.login;
	}
	
 	/**
	 * Accessor Method
 	 * @return email
 	 */
	public String getEmail(){
		return this.email;
	}
	
 	/**
	 * Accessor Method
 	 * @return nome
 	 */
	public String getNome(){
		return this.nome;
	}
	
 	/**
	 * Accessor Method
 	 * @return telefone
 	 */
	public String getTelefone(){
		return this.telefone;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtIncFrom
 	 */
	public Date getDtIncFrom(){
		return this.dtIncFrom;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtIncTo
 	 */
	public Date getDtIncTo(){
		return this.dtIncTo;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtAltFrom
 	 */
	public Date getDtAltFrom(){
		return this.dtAltFrom;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtAltTo
 	 */
	public Date getDtAltTo(){
		return this.dtAltTo;
	}
	
 	/**
	 * Accessor Method
 	 * @return status
 	 */
	public StatusEnum getStatus(){
		return this.status;
	}
	
	

	/**
	 * Mutator Method
 	 * @param login
 	 */
	public void setLogin(String login){
		this.login = login;
	}
	
 	/**
	 * Mutator Method
 	 * @param email
 	 */
	public void setEmail(String email){
		this.email = email;
	}
	
 	/**
	 * Mutator Method
 	 * @param nome
 	 */
	public void setNome(String nome){
		this.nome = nome;
	}
	
 	/**
	 * Mutator Method
 	 * @param telefone
 	 */
	public void setTelefone(String telefone){
		this.telefone = telefone;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtIncFrom
 	 */
	public void setDtIncFrom(Date dtIncFrom){
		this.dtIncFrom = dtIncFrom;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtIncTo
 	 */
	public void setDtIncTo(Date dtIncTo){
		this.dtIncTo = dtIncTo;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtAltFrom
 	 */
	public void setDtAltFrom(Date dtAltFrom){
		this.dtAltFrom = dtAltFrom;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtAltTo
 	 */
	public void setDtAltTo(Date dtAltTo){
		this.dtAltTo = dtAltTo;
	}
	
 	/**
	 * Mutator Method
 	 * @param status
 	 */
	public void setStatus(StatusEnum status){
		this.status = status;
	}
	



	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
return new String[]{
"login", "email", "nome", "telefone", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status"};	}
	
	/* (non-Javadoc)
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
	 * @param freeGrupos the freeGrupos to set
	 */
	public void setFreeGrupos(List<GrupoVO> freeGrupos) {
		this.freeGrupos = freeGrupos;
	}
}