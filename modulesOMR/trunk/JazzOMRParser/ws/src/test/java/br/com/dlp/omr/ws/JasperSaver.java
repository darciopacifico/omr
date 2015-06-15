package br.com.dlp.omr.ws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

public class JasperSaver {

	/**
	 * @param args
	 * @throws JRException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws JRException, FileNotFoundException {
		
		String url = "C:\\darcio\\trabalho\\workspace\\modulesOMR\\trunk\\JazzOMRParser\\parser\\testeCorrectionWS\\WSTestKit\\ExamReportDSP2.jrxml";
		
		FileInputStream fis = new FileInputStream(url);
		
		//InputStream stream = JasperSaver.class.getResourceAsStream("/reports/imgReport.jrxml");

		JasperReport jasperReport = JasperCompileManager.compileReport(fis);
		
		JRSaver.saveObject(jasperReport, "ExamReportDSP2.jasper");

	}

}
