package br.com.dlp.jazzomr.jr.handler;

import java.awt.Color;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementPdfHandler;
import net.sf.jasperreports.engine.export.JRPdfExporterContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class JazzOMRPDFHandler implements GenericElementPdfHandler {
	private static final Logger log = LoggerFactory.getLogger(JazzOMRPDFHandler.class);
	
	
	public JazzOMRPDFHandler() {
	}

	@Override
	public boolean toExport(JRGenericPrintElement element) {
		return false;
	}

	@Override
	public void exportElement(JRPdfExporterContext exporterContext, JRGenericPrintElement element) {
			
		
		Object eqaKey = element.getParameterValue("eqaKey");
		
		PdfWriter pdfWriter = exporterContext.getPdfWriter();
		PdfContentByte canvas = exporterContext.getPdfWriter().getDirectContentUnder();
		
		canvas.saveState();
		canvas.setLineWidth(0.2f);
		//canvas.setLineDash(3f);
		//canvas.setLineDash(0.5f, 0.1f);
		canvas.setColorStroke(Color.white);
		canvas.setColorFill(Color.white);
		
		int x = element.getX() + exporterContext.getOffsetX();
		int y = exporterContext.getExportedReport().getPageHeight() - element.getY() - exporterContext.getOffsetY();
		int x2 = x + element.getWidth();
		int y2 = exporterContext.getExportedReport().getPageHeight() - element.getY() - exporterContext.getOffsetY() - element.getHeight();
		
		int w = x2-x;
		int h = y-y2;
		
		if(log.isDebugEnabled())
				log.debug("id="+eqaKey+" x="+x+",y="+y+",w="+w+",h="+h+"");
		
		canvas.ellipse(x,y,x2,y2);
						
		canvas.fillStroke();
		
		canvas.restoreState();
		
		canvas.saveState();
		
		/*
		BaseFont bf;
		try {
			bf = BaseFont.createFont();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao tentar criar fonte",e);
		}
		canvas.setFontAndSize(bf, 10f);
		canvas.showText("                                                                         id="+obj);
		*/
		canvas.restoreState();
	}

}
