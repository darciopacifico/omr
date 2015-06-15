/**
 * 
 */
package br.com.dlp.jazzomr.poc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author darcio
 * 
 */
public class JasperReportsTest {
	
	/**
	 * @param args
	 * @throws JRException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws JRException, ClassNotFoundException, SQLException {
		
		JasperReport jr = JasperCompileManager.compileReport("../core/src/main/resources/ellipseJRElement.jrxml");
		

		
		
		JasperPrint jp = JasperFillManager.fillReport(jr, new HashMap<String, String>(), JasperReportsTest.getConn());
		
		
		
		JasperExportManager.exportReportToPdfFile(jp, "TesteRPT.pdf");
		
		
	}
	
	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected static Connection getConn() throws ClassNotFoundException,
	SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jazzomr","root","CelularCinza");
		return conn;
	}
	
}
