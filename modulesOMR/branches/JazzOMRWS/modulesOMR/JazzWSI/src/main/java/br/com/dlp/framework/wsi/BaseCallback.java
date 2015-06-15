package br.com.dlp.framework.wsi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.security.auth.callback.CallbackHandler;
import javax.sql.DataSource;

public abstract class BaseCallback implements CallbackHandler {
	
	@SuppressWarnings("unchecked")
	protected Connection getConnection() throws IOException {
		try {
			Connection connection;
			Hashtable ht = new Hashtable();
			ht.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			ht.put(InitialContext.PROVIDER_URL, "jnp://localhost:1199");
			ht.put(InitialContext.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			InitialContext ic = new InitialContext(ht);
			DataSource ds = (DataSource) ic.lookup("java:WSAuthDS");
			connection = ds.getConnection();
			return connection;
		} catch (Exception e) {
			throw new IOException("Erro ao adiquirir conexao com o BD.", e);
		}
		
	}
	
	protected void closeConnection(PreparedStatement stmt, Connection connection, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
