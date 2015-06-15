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
	
	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[]{"dtInclusaoFrom", "dtInclusaoTo", "descricao","nome","dummyVO"};
	}
	@Autowired
	private UsuarioJazzBusiness usuarioJazzBusiness;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private double altura;
	private String login;
	private Date dtInclusaoFrom;
	private Date dtInclusaoTo;
	private String nome;
	
	/**
	 * Pesquisa lista de UsuarioJazzVO e disponibiliza resultados em usuarioJazzVOs
	 */
	@Override
	public String actionPesquisar(){
		
		List<UsuarioJazzVO> resultados = usuarioJazzBusiness.findUsuarioJazzVO(altura, login, dtInclusaoFrom, dtInclusaoTo, nome);
		setResultados(resultados);
		
		return "exibePesquisa";
	}
	
	/**
	 * Accessor Method
	 * @return altura
	 */
	public double getAltura(){
		return altura;
	}
	
	/**
	 * Accessor Method
	 * @return login
	 */
	public String getLogin(){
		return login;
	}
	
	/**
	 * Accessor Method
	 * @return dtInclusaoFrom
	 */
	public Date getDtInclusaoFrom(){
		return dtInclusaoFrom;
	}
	
	/**
	 * Accessor Method
	 * @return dtInclusaoTo
	 */
	public Date getDtInclusaoTo(){
		return dtInclusaoTo;
	}
	
	/**
	 * Accessor Method
	 * @return nome
	 */
	public String getNome(){
		return nome;
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
		return usuarioJazzBusiness;
	}
}