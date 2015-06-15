package br.com.dlp.jazz.svnhook.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import br.com.dlp.jazz.svnhook.vos.IssueVO;
import br.com.dlp.jazz.svnhook.vos.ProjectVO;

/**
 * Mapeia issue consultada via query jdbc
 * @author t_dpacifico
 */
public class IssueVORowMapper implements ParameterizedRowMapper<IssueVO> {

	/**
	 * Mapeia issue, apenas campos relevantes para o hooking de repositorio
	 */
	@Override
	public IssueVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		IssueVO issueVO = new IssueVO();
		ProjectVO projectVO = new ProjectVO();
					
		issueVO.setId((Long) rs.getLong("id"));
		issueVO.setClosed(rs.getBoolean("is_closed"));
		issueVO.setProjectVO(projectVO);
		
		projectVO.setId(rs.getLong("project_id"));
		projectVO.setName(rs.getString("name"));
		projectVO.setIdParentProject(rs.getLong("parent_id")); 
		
		return issueVO;
	}

}