/**
 * 
 */
package br.com.dlp.jazz.svnhook.vos;

import java.io.Serializable;

/**
 * Projeto do redmine
 * @author t_dpacifico
 *
 */
public class ProjectVO implements Serializable {

	private static final long serialVersionUID = 5243925930404619074L;
	private Long id;
	private String name;
	private Long idParentProject;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @return the nome
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @param name the nome to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.id+"-"+this.name;
	}


	/**
	 * @return the idParentProject
	 */
	public Long getIdParentProject() {
		return idParentProject;
	}


	/**
	 * @param idParentProject the idParentProject to set
	 */
	public void setIdParentProject(Long idParentProject) {
		this.idParentProject = idParentProject;
	}



}
