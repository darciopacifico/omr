/**
 * 
 */
package br.com.dlp.jazzomr.poc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.dlp.jazzomr.jasper.JazzOMRJRDataSourceProvider;
import br.com.dlp.jazzomr.question.ShuffleQuestionsVisitor;
import br.com.jazz.jrds.JazzJRDataSource;

/**
 * @author darcio
 * 
 */
public class JasperReportsTestDSP {
	public static void main(String[] args) throws JRException, ClassNotFoundException, SQLException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		JazzOMRJRDataSourceProvider provider = new JazzOMRJRDataSourceProvider(map);
		
		JazzJRDataSource ds = provider.getJazzJRDataSource();
		
		int i=0;
		while(ds.next()){
			i++;
			JRField field = ds.getFields()[35];
			
			
			Object fieldValue = ds.getFieldValue(field);
			System.out.println("[registro "+i+"] "+ field.getName() + " = "+fieldValue + " class ="+field.getValueClassName());
		}
		
		System.out.println("--------------------------------------------------------");
	}
}
