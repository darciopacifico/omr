/**
 * 
 */
package br.com.dlp.jazz.svnhook.vos;

import java.io.Serializable;

/**
 * Informacoes relevantes para issues encontradas no redmine.
 * @author t_dpacifico
 *
 */
public class IssueVO implements Serializable {

	private static final long serialVersionUID = -8852157598509727474L;
	
	private Long id;
	
	private ProjectVO projectVO;
	
	
	private Boolean closed=true;
	
	/**
	 * Default constructor 
	 */
	public IssueVO() {
		//explicity empty
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}




	/**
	 * @return the issueAberta
	 */
	public Boolean getClosed() {
		return closed;
	}


	/**
	 * @param issueAberta the issueAberta to set
	 */
	public void setClosed(Boolean issueAberta) {
		this.closed = issueAberta;
	}




	
	
	@Override
	public String toString() {
		return "#"+id;
	}

	


	/**
	 * @return the projectVO
	 */
	public ProjectVO getProjectVO() {
		return projectVO;
	}


	/**
	 * @param projectVO the projectVO to set
	 */
	public void setProjectVO(ProjectVO projectVO) {
		this.projectVO = projectVO;
	}
}
