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
		
		/*
		132 = pes.nome
		95 = perg.description
		82 = crt.description
		9 = description
		 */
		
		while(ds.next()){
			JRField[] fields = ds.getFields();
			
			for (JRField jrField : fields) {
				System.out.print(ds.getFieldValue(jrField)+"|");
			}

			/*
			System.out.print(ds.getFieldValue(fields[132])+"|");
			System.out.print(ds.getFieldValue(fields[95])+"|");
			System.out.print(ds.getFieldValue(fields[82])+"|");
			System.out.print(ds.getFieldValue(fields[9])+"|");
			*/
			System.out.println();
		}
	}
	
}
