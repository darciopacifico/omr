package br.com.dlp.omr.ws;

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
	 */
	public static void main(String[] args) throws JRException {
		
		InputStream stream = JasperSaver.class.getResourceAsStream("/reports/ExamReportDSP2.jrxml");

		JasperReport jasperReport = JasperCompileManager.compileReport(stream);
		
		JRSaver.saveObject(jasperReport, "ExamReportDSP2.jasper");

	}

}
