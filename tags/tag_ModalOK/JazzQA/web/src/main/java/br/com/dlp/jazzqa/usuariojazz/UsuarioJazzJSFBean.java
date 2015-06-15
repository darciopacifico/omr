/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente UsuarioJazzVO
 *
 **/
@Component
public class UsuarioJazzJSFBean extends AbstractJSFBeanImpl<UsuarioJazzVO> {
	
	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private UsuarioJazzBusiness usuarioJazzBusiness;

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String nome;
 	private String login;

	/**
	 * Pesquisa lista de UsuarioJazzVO e disponibiliza resultados em usuarioJazzVOs 
	 */
	public String actionPesquisar(){
	
		List<UsuarioJazzVO> resultados = usuarioJazzBusiness.findUsuarioJazzVO(nome, login);
		setResultados(resultados);

		return "exibePesquisa";
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
 	 * @return login
 	 */
	public String getLogin(){
		return this.login;
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
 	 * @param login
 	 */
	public void setLogin(String login){
		this.login = login;
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<UsuarioJazzVO> getBusiness() {
		return this.usuarioJazzBusiness;
	}
}