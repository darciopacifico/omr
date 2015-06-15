
package main.test.com.msaf.validador.core.lerXLS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;




public class LeitorXLSClienteTest {

//	protected static ApplicationContext getContext() {return context;}
	
//	 private static final String[] CONTEXT_CONFIG_LOCATION = {"test/dataAccessContext-test.xml"};
//	 private static final ApplicationContext context = new FileSystemXmlApplicationContext(CONTEXT_CONFIG_LOCATION);
	 
	 public static void main(String[] args){
	 // ApplicationContext context = getContext();
	  //Repositorio repositorio = (Repositorio) context.getBean("repositorio");

	 // LeitorXLSCliente leitor = new LeitorXLSCliente();	  
	  HSSFWorkbook wb = new HSSFWorkbook();
	  
	  File file = new File("C:/teste.xls");
	  OutputStream outputStream;
	try {
		outputStream = new FileOutputStream(file);
		//outputStream = leitor.gerarArquivoXLS(new Long(132), outputStream);
		//wb.write(outputStream);
		outputStream.close();
			
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  

	   System.out.println("Fim");

	 }
	 
	
	
}
