import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

public class JasperTeste {

	/**
	 * @param args
	 * @throws JRException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JRException, IOException {
		
		//createPDF();
		createImage();
	}

	
	
	
	public static void createPDF() throws FileNotFoundException, JRException, IOException {
		// TODO Auto-generated method stub

		InputStream jrxmlStream = new FileInputStream("/home/darcio/workspace/modules/JazzAV/bsn/src/main/resources/modeloAdes1.jrxml");

		JasperReport compiled = JasperCompileManager.compileReport(jrxmlStream);

		
		Map rootMap = new HashMap();
		JasperPrint jasperPrint = JasperFillManager.fillReport(compiled, rootMap, new JREmptyDataSource());

		int pageIndex = 0;

		JRPdfExporter exporter = new JRPdfExporter();
		
		
		exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
	
		exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE, new File("/home/darcio/workspace/modules/JazzAV/bsn/src/main/resources/modeloAdes1.pdf"));
		
		
		
		exporter.exportReport();

	
	}
	
	
	public static void createImage() throws FileNotFoundException, JRException, IOException {
		// TODO Auto-generated method stub

		InputStream jrxmlStream = new FileInputStream("/home/darcio/workspace/modules/JazzAV/bsn/src/main/resources/modeloAdes1.jrxml");

		FileOutputStream out = new FileOutputStream("/home/darcio/workspace/modules/JazzAV/bsn/src/main/resources/modeloAdes1.png");


		JasperReport compiled = JasperCompileManager.compileReport(jrxmlStream);

		
		Map rootMap = new HashMap();
		JasperPrint jasperPrint = JasperFillManager.fillReport(compiled, rootMap, new JREmptyDataSource(1));

		int pageIndex = 0;

		BufferedImage pageImage = new BufferedImage(jasperPrint.getPageWidth() + 1, jasperPrint.getPageHeight() + 1, BufferedImage.TYPE_INT_RGB);

		JRGraphics2DExporter exporter = new JRGraphics2DExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, pageImage.getGraphics());

		
		exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, 0.6f);
		
		
		exporter.exportReport();
		ImageIO.write(pageImage, "png", out);
	}

}
