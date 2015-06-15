/**
 * 
 */
package br.com.dlp.jazz.svnhook.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import br.com.dlp.jazz.svnhook.vos.ProjectVO;
import br.com.dlp.jazz.svnhook.vos.RepositorioVO;

/**
 * Mapeia resultado de consulta de repositorios para lista de repositorios.
 * 
 * @author t_dpacifico
 * 
 */
public class RepositorioRowMapper implements ParameterizedRowMapper<RepositorioVO> {

	private Boolean useUUID = true;

	/**
	 * @param useUUID
	 */
	public RepositorioRowMapper(Boolean useUUID) {
		this.useUUID = useUUID;
	}

	/**
	 * @param useUUID
	 */
	public RepositorioRowMapper() {
		//explicity empty
	}

	@Override
	public RepositorioVO mapRow(ResultSet rs, int rowNum) throws SQLException {

		ProjectVO projetoVO = new ProjectVO();
		projetoVO.setId(rs.getLong("id"));
		projetoVO.setName(rs.getString("name"));
		projetoVO.setIdParentProject(rs.getLong("parent_id"));

		RepositorioVO repositorioVO = new RepositorioVO();

		if (this.useUUID) {
			repositorioVO.setUuid(rs.getString("repouuid"));
		}else{
			repositorioVO.setUuid("NaoInformado");
		}

		repositorioVO.setRepositoryId(rs.getLong("repository_id"));
		repositorioVO.setUrl(rs.getString("url"));
		repositorioVO.setRootUrl(rs.getString("root_url"));
		repositorioVO.setProjetoVO(projetoVO);

		return repositorioVO;

	}

	/**
	 * @return the useUUID
	 */
	public Boolean getUseUUID() {
		return useUUID;
	}

	/**
	 * @param useUUID the useUUID to set
	 */
	public void setUseUUID(Boolean useUUID) {
		this.useUUID = useUUID;
	}
}
