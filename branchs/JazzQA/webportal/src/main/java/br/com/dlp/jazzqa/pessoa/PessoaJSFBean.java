/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.pessoa;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente PessoaVO
 *
 **/
@Scope(value="session")
@Component
public class PessoaJSFBean extends AbstractJSFBeanImpl<PessoaVO> {
	
	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[]{"dtInclusaoFrom", "dtInclusaoTo", "descricao","nome","dummyVO"};
	}
	private static final long serialVersionUID = 2195241915100521959L;
	
	@Autowired
	private PessoaBusiness pessoaBusiness;
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String login;
	private String email;
	private String nome;
	private String telefone;
	private Date dtInc;
	private Date dtAlt;
	private StatusEnum status;
	
	/**
	 * Pesquisa lista de PessoaVO e disponibiliza resultados em pessoaVOs
	 */
	@Override
	public String actionPesquisar(){
		
		List<PessoaVO> resultados = pessoaBusiness.findPessoaVO(login, email, nome, telefone, dtInc, dtAlt, status);
		setResultados(resultados);
		
		return "exibePesquisa";
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
	 * @return email
	 */
	public String getEmail(){
		return email;
	}
	
	/**
	 * Accessor Method
	 * @return nome
	 */
	public String getNome(){
		return nome;
	}
	
	/**
	 * Accessor Method
	 * @return telefone
	 */
	public String getTelefone(){
		return telefone;
	}
	
	/**
	 * Accessor Method
	 * @return dtInc
	 */
	public Date getDtInc(){
		return dtInc;
	}
	
	/**
	 * Accessor Method
	 * @return dtAlt
	 */
	public Date getDtAlt(){
		return dtAlt;
	}
	
	/**
	 * Accessor Method
	 * @return status
	 */
	public StatusEnum getStatus(){
		return status;
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
	 * @param dtInc
	 */
	public void setDtInc(Date dtInc){
		this.dtInc = dtInc;
	}
	
	/**
	 * Mutator Method
	 * @param dtAlt
	 */
	public void setDtAlt(Date dtAlt){
		this.dtAlt = dtAlt;
	}
	
	/**
	 * Mutator Method
	 * @param status
	 */
	public void setStatus(StatusEnum status){
		this.status = status;
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<PessoaVO> getBusiness() {
		return pessoaBusiness;
	}
}