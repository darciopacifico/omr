package br.com.dlp.jazz.svnhook.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import br.com.dlp.jazz.svnhook.vos.RedmineUserVO;


/**
 * Mapeia um resultset para RedmineUserVO
 * @author t_dpacifico
 *
 */
public class RedmineUserRowMapper implements ParameterizedRowMapper<RedmineUserVO> {

	/**
	 * Mapeia os resultados para um RedmineUserVO
	 */
	@Override
	public RedmineUserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		RedmineUserVO redmineUserVO = new RedmineUserVO();
		
		redmineUserVO.setId(rs.getLong("id"));
		redmineUserVO.setLogin(rs.getString("login"));
		redmineUserVO.setStatus(rs.getInt("status"));
		
		return redmineUserVO;
	}

}
