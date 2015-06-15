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
 * Callback para testes de segurança em webservices
 * 
 * @author t_dpacifico
 * 
 */
public class MockPasswordCallback extends BaseCallback {
	
	private String codServico;
	
	private final static String sql = "select 1 " + "from aplicacao apli, " + "     aplicacao_servico asrv, " + "     servico srv "
			+ "where apli.cd_aplicacao = asrv.cd_aplicacao " + "AND   srv.cd_servico = asrv.cd_servico " + "AND   apli.ic_ativo = 1 "
			+ "AND   srv.ic_ativo = 1 " + "AND   asrv.dt_inicio_validade <= trunc(sysdate) " + "AND   (asrv.dt_fim_validade >= trunc(sysdate) "
			+ "    OR asrv.dt_fim_validade is null) " + "AND    apli.nm_senha = ? " + "AND   apli.cd_aplicacao = ? "
			+ "AND   srv.cd_servico = ? ";
	
	/**
	 * Constroi call back de usuario e senha recebendo o id do serviço
	 * 
	 * @param codServico
	 */
	public MockPasswordCallback(String codServico) {
		this.codServico = codServico.toUpperCase();
	}
	
	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		
		String usuario = pc.getIdentifier();
		String senha = pc.getPassword();
		
		if (!(usuario.equals("usuario") && senha.equals("senha"))) {
			throw new IOException("O usuário ou senha inválidos!");
		}
	}
	
	/**
	 * Confronta usuário e senha com base de dados de usuários
	 * 
	 * @param pc
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void checkAutentication(WSPasswordCallback pc) throws IOException {
		// Implementa��o para validar se a aplica��o tem acesso ao servi�o e se
		// a senha est� correta
		if (pc.getIdentifier() == null) {
			throw new IOException("Chamada n�o autorizada.");
		}
		
		String codAplicacao = pc.getIdentifier().toUpperCase();
		// try {
		// codAplicacao = Integer.parseInt(pc.getIdentifier());
		// } catch (NumberFormatException nfe) {
		// throw new IOException("C�digo de aplica��o inv�lido");
		// }
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = connection.prepareStatement(MockPasswordCallback.sql);
			stmt.setString(1, pc.getPassword());
			stmt.setString(2, codAplicacao);
			stmt.setString(3, codServico);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				return;
			} else {
				throw new IOException("Chamada n�o autorizada.");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new IOException("Erro na validacao.");
		} finally {
			closeConnection(stmt, connection, rs);
		}
	}
	
	public String getCodServico() {
		return codServico;
	}
	
	public void setCodServico(String codServico) {
		this.codServico = codServico;
	}
}
