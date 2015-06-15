package br.com.dlp.framework.wsi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * Implementação simulada de callback para inserção de usuário e senha
 * 
 * @author t_dpacifico
 * 
 */
public class MockPasswordCallbackClient extends BaseCallback {
	
	private final static String sql = "select nm_senha from aplicacao where cd_aplicacao = ?";
	
	/**
	 * Atribui senha da aplicação ao WSPasswordCallback
	 */
	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		pc.setPassword("senha");
		// atribuiSenha(pc);
		
	}
	
	/**
	 * Atribui senha para o código de aplicação cadastrado
	 * 
	 * @param pc
	 * @throws IOException
	 */
	protected void atribuiSenha(WSPasswordCallback pc) throws IOException {
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = connection.prepareStatement(MockPasswordCallbackClient.sql);
			stmt.setString(1, pc.getIdentifier().toUpperCase());
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				pc.setPassword(rs.getString("nm_senha"));
				return;
			} else {
				throw new IOException("Aplicativo nï¿½o cadastrado.");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new IOException("Erro ao buscar senha.");
		} finally {
			closeConnection(stmt, connection, rs);
		}
	}
	
}
