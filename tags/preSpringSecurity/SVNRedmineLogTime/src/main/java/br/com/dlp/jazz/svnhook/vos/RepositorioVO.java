package br.com.dlp.jazz.svnhook.vos;

import java.io.Serializable;

/**
 * Repositorio associado a um projeto no redmine
 * @author t_dpacifico
 *
 */
public class RepositorioVO implements Serializable {
	
	private static final long serialVersionUID = 7425266323065955197L;

	private Long repositoryId;
	private String url;
	private String rootUrl;
	private String uuid;

	private ProjectVO projetoVO;
	/**
	 * @return the repositoryId
	 */
	public Long getRepositoryId() {
		return repositoryId;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the rootUrl
	 */
	public String getRootUrl() {
		return rootUrl;
	}
	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(Long repositoryId) {
		this.repositoryId = repositoryId;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @param rootUrl the rootUrl to set
	 */
	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}
	
	/**
	 * 
	 * @param projetoVO
	 */
	public void setProjetoVO(ProjectVO projetoVO) {
		this.projetoVO = projetoVO;
	}
	/**
	 * @return the projetoVO
	 */
	public ProjectVO getProjetoVO() {
		return projetoVO;
	}
	

	/**
	 * @return the rootuuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param rootuuid the rootuuid to set
	 */
	public void setUuid(String rootuuid) {
		this.uuid = rootuuid;
	}
	
	@Override
	public String toString() {
		return this.url+" "+this.projetoVO;
	}
}
