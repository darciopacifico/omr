/**
 * 
 */
package br.com.dlp.jazzomr.poc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import br.com.dlp.jazzomr.jasper.JazzOMRJRDataSourceProvider;
import br.com.dlp.jazzomr.jasper.JazzOMRJRDataSourceProviderForDesign;
import br.com.dlp.jazzomr.question.ShuffleQuestionsVisitor;

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
		
		InputStream stream = JasperReportsTest.class.getResourceAsStream("/reports/ExamReportDSP2.jrxml");
		
		JasperReport jr = JasperCompileManager.compileReport(stream);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		JazzOMRJRDataSourceProviderForDesign sample = new JazzOMRJRDataSourceProviderForDesign(map );
		
		
		JasperPrint jp = JasperFillManager.fillReport(jr, null, sample.getJazzJRDataSource());
		
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
