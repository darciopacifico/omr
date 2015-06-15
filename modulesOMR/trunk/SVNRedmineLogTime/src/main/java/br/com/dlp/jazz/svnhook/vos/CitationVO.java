/**
 * 
 */
package br.com.dlp.jazz.svnhook.vos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookRTException;

/**
 * Citacao efetuada num commit de artefatos num repositório.
 * Um comentário pode conter várias citacoes de issues, por tanto, será criada uma instância desta classe para cada citação encontrada.
 * @author t_dpacifico
 */
public class CitationVO implements Serializable {
	
	private static final long serialVersionUID = 1186507378864860939L;

	/**
	 * Comandos aceitos para citacao de issue
	 * @author t_dpacifico
	 */
	public enum COMANDOS {
		FIXES("fixes"), CLOSES("closes"), REFS("refs");
		private String comando;
		COMANDOS(String comando){
			this.comando=comando;
		}
		/**
		 * @return the comando
		 */
		public String getComando() {
			return comando;
		}
		/**
		 * @param comando the comando to set
		 */
		public void setComando(String comando) {
			this.comando = comando;
		}
	}
	
	private COMANDOS comando;
	private Long issue;
	private String login;
	private Long horasDespendidas;
	private IssueVO issueVO;
	private RedmineUserVO redmineUserVO;
	
	private List<CriticVO> critics = new ArrayList<CriticVO>(2);
	
	/**
	 * Construtor padrão
	 */
	public CitationVO() {
		//explicity empty
	}
	
	/**
	 * Constroi citacao de issue
	 * @param pComando
	 * @param issue
	 * @param horasDespendidas
	 */
	public CitationVO(COMANDOS pComando, Long issue, Long horasDespendidas, String login) {
		super();
		this.comando = pComando;
		this.issue = issue;
		this.horasDespendidas = horasDespendidas;
		this.login = login;
	}
	

	/**
	 * Constroi citacao de issue
	 * @param comando
	 * @param issue
	 * @param horasDespendidas
	 */
	public CitationVO(String strComando, String strIssue, String strEsforco, String login) {

		COMANDOS lComando=null;
		Long lIssue; //nao pode ser nulo
		Long esforcoH=null; //pode ser nulo, mas um pelo menos deve citar o esforco
		//cria o objeto enum
		lComando = COMANDOS.valueOf(strComando.toUpperCase());
		//confirma se foi criado com sucesso
		if( lComando==null ){
			throw new JazzSVNHookRTException("O comando informado ("+strComando+") nao eh reconhecido como um comando válido ("+COMANDOS.values()+")!");
		}
		
		
		//checa se foi informado
		if (NumberUtils.isNumber(strIssue)){
			lIssue = NumberUtils.createLong(strIssue);
		}else{
			throw new JazzSVNHookRTException("O issue informado ("+strIssue+") nao eh reconhecido como um numero!");
		}

		
		//se informado, parseia o valor
		if(NumberUtils.isNumber(strEsforco)){
			esforcoH = NumberUtils.createLong(strEsforco);
		}
					
		this.comando=lComando;
		this.issue =lIssue;
		this.horasDespendidas=esforcoH;
		this.login = login;

	}
	
	/**
	 * Constroi citacao de issue
	 * @param comando
	 * @param issue
	 */
	public CitationVO(COMANDOS comando, Long issue) {
		super();
		this.comando = comando;
		this.issue = issue;
	}
	
	
	
	/**
	 * @return the comando
	 */
	public COMANDOS getComando() {
		return comando;
	}

	/**
	 * @return the issue
	 */
	public Long getIssue() {
		return issue;
	}

	/**
	 * @return the horasDespendidas
	 */
	public Long getHorasDespendidas() {
		return horasDespendidas;
	}

	/**
	 * @param comando the comando to set
	 */
	public void setComando(COMANDOS comando) {
		this.comando = comando;
	}

	/**
	 * @param issue the issue to set
	 */
	public void setIssue(Long issue) {
		this.issue = issue;
	}

	/**
	 * @param horasDespendidas the horasDespendidas to set
	 */
	public void setHorasDespendidas(Long horasDespendidas) {
		this.horasDespendidas = horasDespendidas;
	}

	/**
	 * @return the issueVO
	 */
	public IssueVO getIssueVO() {
		return issueVO;
	}

	/**
	 * @param issueVO the issueVO to set
	 */
	public void setIssueVO(IssueVO issueVO) {
		this.issueVO = issueVO;
	}

	/**
	 * @return the critics
	 */
	public List<CriticVO> getCritics() {
		return critics;
	}

	/**
	 * @param critics the critics to set
	 */
	public void setCritics(List<CriticVO> critics) {
		this.critics = critics;
	}


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	@Override
	public String toString() {
		return "Author:"+this.login + " \""+this.comando + " " + this.issueVO+"\"";
	}

	/**
	 * @return the redmineUserVO
	 */
	public RedmineUserVO getRedmineUserVO() {
		return redmineUserVO;
	}

	/**
	 * @param redmineUserVO the redmineUserVO to set
	 */
	public void setRedmineUserVO(RedmineUserVO redmineUserVO) {
		this.redmineUserVO = redmineUserVO;
	}
	
}
