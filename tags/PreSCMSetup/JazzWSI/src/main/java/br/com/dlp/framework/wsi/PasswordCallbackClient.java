package br.com.dlp.framework.wsi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class PasswordCallbackClient extends BaseCallback {

	private final static String sql = "select nm_senha from aplicacao where cd_aplicacao = ?";

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		String codApp = pc.getIdentifier().toUpperCase();
//		int codApp = Integer.parseInt(appName);
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, codApp);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				pc.setPassword(rs.getString("nm_senha"));
				return;
			} else {
				throw new IOException("Aplicativo n�o cadastrado.");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new IOException("Erro ao buscar senha.");
		} finally {
			closeConnection(stmt, connection, rs);
		}

	}
	
}
