/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.jazzqa.status.StatusVO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente UsuarioJazzVO
 *
 **/
@Scope(value="session")
@Component
public class UsuarioJazzJSFBean extends AbstractJSFBeanImpl<UsuarioJazzVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private UsuarioJazzBusiness usuarioJazzBusiness;

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private StatusVO status;
 	private List<StatusVO> stati;
 	private double altura;
 	private String login;
 	private Date dtInclusaoFrom;
 	private Date dtInclusaoTo;
 	private String nome;

	/**
	 * Pesquisa lista de UsuarioJazzVO e disponibiliza resultados em usuarioJazzVOs 
	 */
	public String actionPesquisar(){
	
		List<UsuarioJazzVO> resultados = usuarioJazzBusiness.findUsuarioJazzVO(status, stati, altura, login, dtInclusaoFrom, dtInclusaoTo, nome);
		setResultados(resultados);

		return "exibePesquisa";
	}

	/**
	 * Accessor Method
 	 * @return status
 	 */
	public StatusVO getStatus(){
		return this.status;
	}
	
 	/**
	 * Accessor Method
 	 * @return stati
 	 */
	public List<StatusVO> getStati(){
		return this.stati;
	}
	
 	/**
	 * Accessor Method
 	 * @return altura
 	 */
	public double getAltura(){
		return this.altura;
	}
	
 	/**
	 * Accessor Method
 	 * @return login
 	 */
	public String getLogin(){
		return this.login;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtInclusaoFrom
 	 */
	public Date getDtInclusaoFrom(){
		return this.dtInclusaoFrom;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtInclusaoTo
 	 */
	public Date getDtInclusaoTo(){
		return this.dtInclusaoTo;
	}
	
 	/**
	 * Accessor Method
 	 * @return nome
 	 */
	public String getNome(){
		return this.nome;
	}
	
	/**
	 * Mutator Method
 	 * @param status
 	 */
	public void setStatus(StatusVO status){
		this.status = status;
	}
	
 	/**
	 * Mutator Method
 	 * @param stati
 	 */
	public void setStati(List<StatusVO> stati){
		this.stati = stati;
	}
	
 	/**
	 * Mutator Method
 	 * @param altura
 	 */
	public void setAltura(double altura){
		this.altura = altura;
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
 	 * @param dtInclusaoFrom
 	 */
	public void setDtInclusaoFrom(Date dtInclusaoFrom){
		this.dtInclusaoFrom = dtInclusaoFrom;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtInclusaoTo
 	 */
	public void setDtInclusaoTo(Date dtInclusaoTo){
		this.dtInclusaoTo = dtInclusaoTo;
	}
	
 	/**
	 * Mutator Method
 	 * @param nome
 	 */
	public void setNome(String nome){
		this.nome = nome;
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<UsuarioJazzVO> getBusiness() {
		return this.usuarioJazzBusiness;
	}
}