/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.empresa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRJSFBeanImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.person.EAuthority;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente EmpresaVO
 *
 **/
@Scope(value="session")
@Component
public class EmpresaJSFBean extends AbstractJazzOMRJSFBeanImpl<EmpresaVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private EmpresaBusiness empresaBusiness;


	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */

	private String nome;
 	private Date dtIncFrom;
 	private Date dtIncTo;
 	private Date dtAltFrom;
 	private Date dtAltTo;
 	private StatusEnum status;

	
	public List<? extends Enum> autorizeAnyOf(){
		ArrayList<EAuthority> authorities = new ArrayList<EAuthority>();
		authorities.add(EAuthority.MASTER_ADM);
		return authorities;
	}

	
	@Override
	public Boolean isAutorized() {
		
		String loginName = getSecurity().getLoginName();
		
		return ("dpacifico".equals(loginName));
	}
	
	
	
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo.
	 * Pesquisa e retorna lista de EmpresaVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<EmpresaVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
	
		List<EmpresaVO> resultados = empresaBusiness.findEmpresaVO(nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
		
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
		return empresaBusiness.countEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);	}
	

	/**
	 * Accessor Method
 	 * @return nome
 	 */
	public String getNome(){
		return this.nome;
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
 	 * @param nome
 	 */
	public void setNome(String nome){
		this.nome = nome;
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
"nome", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status"};	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<EmpresaVO> getBusiness() {
		return this.empresaBusiness;
	}
}