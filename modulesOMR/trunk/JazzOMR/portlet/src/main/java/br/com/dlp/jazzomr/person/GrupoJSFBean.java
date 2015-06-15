/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DualListModel;
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
 * Implementação de Bean JSF para o componente GrupoVO
 *
 **/
@Scope(value="session")
@Component
public class GrupoJSFBean extends AbstractJazzOMRJSFBeanImpl<GrupoVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private GrupoBusiness grupoBusiness;
	
	@Autowired
	private PessoaBusiness pessoaBusiness;

	private List<PessoaVO> freePessoas;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */

	private String description;
 	private Date dtIncFrom;
 	private Date dtIncTo;
 	private Date dtAltFrom;
 	private Date dtAltTo;
 	private StatusEnum status;

 	private DualListModel<PessoaVO> pessoaDLM;
 	private DualListModel<EAuthority> permissaoDLM;
 	
 	
 	
 	/**
 	 * Cria controlador para o picklist de permissoes do grupo
 	 * @return
 	 */
	public DualListModel<EAuthority> getPermissaoDLM() {
		
		if(permissaoDLM==null){
			createPermissoesDLM(tmpVO);
		}
		
		return permissaoDLM;
	}


	public void setPermissaoDLM(DualListModel<EAuthority> permissaoDLM) {
		this.permissaoDLM = permissaoDLM;
		if(getTmpVO()!=null &&  permissaoDLM!=null) {
			getTmpVO().setAuthorities(permissaoDLM.getTarget());
		}
	}




	/**
	 * Gera DLM para picklist de pessoas que farao parte do grupo
	 * @return
	 */
	public DualListModel<PessoaVO> getPessoaDLM() {
		
		if(pessoaDLM==null){
			createPessoaDLM(tmpVO);
		}
		
		return pessoaDLM;
	}

	
	
	
	public void setPessoaDLM(DualListModel<PessoaVO> pessoalDLM) {
		this.pessoaDLM = pessoalDLM;
	
		if(getTmpVO()!=null && this.pessoaDLM!=null){
			getTmpVO().setPessoas(this.pessoaDLM.getTarget());
		}
		
	}

	
	
	
	
	public List<? extends Enum> autorizeAnyOf(){
		
		ArrayList<EAuthority> authorities = new ArrayList<EAuthority>();
		
		authorities.add(EAuthority.MASTER_ADM);
		
		return authorities;
		
	}
	
	/**
	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar...
	 * @return
	 */
	public List<SelectItem> getStatusList() {
		return getEnums(StatusEnum.class);
	}

 	@Override
 	public void setTmpVO(GrupoVO tmpVO) {
 		
 		if(tmpVO!=null ){
 			if(tmpVO.getPK()!=null){
 				IJazzOMRBusiness<GrupoVO> business = (IJazzOMRBusiness<GrupoVO>) getBusiness();
 				tmpVO = business.findByPK(tmpVO, "grupo_com_pessoas");
 			}
 			
 			createPessoaDLM(tmpVO);
 			createPermissoesDLM(tmpVO);
 			
 		}

 		
 		
 		super.setTmpVO(tmpVO);
 	}


 	/**
 	 * 
 	 * @param tmpVO
 	 */
	private void createPessoaDLM(GrupoVO tmpVO) {

		List<PessoaVO> pessoasIn;
		if(tmpVO==null || tmpVO.getPessoas()==null){
			pessoasIn = new ArrayList<PessoaVO>();
		}else{
			pessoasIn = tmpVO.getPessoas();
		}
		
		List<PessoaVO> pessoasNotIn = pessoaBusiness.findAllNotIn(pessoasIn);
		
		DualListModel<PessoaVO> pessoaDLM = new DualListModel<PessoaVO>();
		pessoaDLM.setSource(pessoasNotIn);
		pessoaDLM.setTarget(pessoasIn);
		
		this.pessoaDLM = pessoaDLM;
		
	}
	
	

 	/**
 	 * 
 	 * @param tmpVO
 	 */
	private void createPermissoesDLM(GrupoVO tmpVO) {

		List<EAuthority> permissoesIn;
		if(tmpVO==null || tmpVO.getAuthorities()==null){
			permissoesIn = new ArrayList<EAuthority>();
		}else{
			permissoesIn = tmpVO.getAuthorities();
		}
		
		List<EAuthority> authsNotIn = getEnumsList(EAuthority.class);
		if(CollectionUtils.isNotEmpty(permissoesIn)){
			authsNotIn.removeAll(permissoesIn);
		}
		
		
		DualListModel<EAuthority> permissaoDLM = new DualListModel<EAuthority>();
		permissaoDLM.setSource(authsNotIn);
		permissaoDLM.setTarget(permissoesIn);
		
		this.permissaoDLM = permissaoDLM;
		
	}




	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo.
	 * Pesquisa e retorna lista de GrupoVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<GrupoVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		List<GrupoVO> resultados = grupoBusiness.findGrupoVO(description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);		
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
		return grupoBusiness.countGrupoVO(description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);	
	}
	

	/**
	 * Accessor Method
 	 * @return description
 	 */
	public String getDescription(){
		return this.description;
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
 	 * @param description
 	 */
	public void setDescription(String description){
		this.description = description;
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
"description", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status"};	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<GrupoVO> getBusiness() {
		return this.grupoBusiness;
	}

		
	public List<EAuthority> getAuts() {
		List<EAuthority> enumsList = getEnumsList(EAuthority.class);
		if(getTmpVO()!=null && getTmpVO().getAuthorities()!=null){
			enumsList.removeAll(getTmpVO().getAuthorities());
		}
		return enumsList;
	}

	public void setAuts(List<EAuthority> auts) {
	}	
}