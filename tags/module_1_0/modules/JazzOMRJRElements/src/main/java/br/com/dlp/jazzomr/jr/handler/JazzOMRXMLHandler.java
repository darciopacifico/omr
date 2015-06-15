package br.com.dlp.jazzomr.jr.handler;

import java.awt.Color;

import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.base.JRBasePrintEllipse;
import net.sf.jasperreports.engine.base.JRBasePrintRectangle;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.export.GenericElementXmlHandler;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementação de Jasper GenericElementXmlHandler, para manipular saída XML de elemento um genérico que está no relatório de provas. Esta saída em
 * XML deve exportar um elemento gráfico (elipse ou retangulo). O atributo key deste elemento deve conter a identificação da alternativa e da pagina
 * onde esta esta inserida.
 * 
 * Posteriormente este XML será processado via SAX e as coordenadas e páginas de cada questão e alternativa serão armazenadas em banco de dados.
 * 
 * Estas coordenadas armazenadas em banco de dados serão utilizadas como gabarito para parsear as imagens de provas scaneadas.
 * 
 * @author darcio
 * 
 */
public class JazzOMRXMLHandler implements GenericElementXmlHandler {

	private static final Logger log = LoggerFactory.getLogger(JazzOMRXMLHandler.class);

	@Override
	public boolean toExport(JRGenericPrintElement jrGenericPrintElement) {
		return true;
	}

	/**
	 * Especialização de GenericElementXmlHandler
	 */
	@Override
	public void exportElement(JRXmlExporterContext exporterContext, JRGenericPrintElement element) {

		Object eqaKey = element.getParameterValue("eqaKey");
		
		exportAlternativeBullet(exporterContext, element, eqaKey);

	}

	
	/**
	 * Exporta bullet para alternativa de questao
	 * @param exporterContext
	 * @param element
	 * @param eqaKey
	 */
	protected void exportAlternativeBullet(JRXmlExporterContext exporterContext, JRGenericPrintElement element, Object eqaKey ) {
		int x = element.getX() + exporterContext.getOffsetX();
		int y = element.getY() - exporterContext.getOffsetY();
		int x2 = x + element.getWidth();
		int y2 = element.getY() - exporterContext.getOffsetY() - element.getHeight();

		int w = x2 - x;
		int h = y - y2;

		JRXmlExporter xmlExporter = (JRXmlExporter) exporterContext.getExporter();
		JRDefaultStyleProvider defaultStyleProvider = element.getDefaultStyleProvider();

		if (log.isDebugEnabled()) {
			log.debug("id=" + eqaKey + " x=" + x + ",y=" + y + ",w=" + w + ",h=" + h + "");
		}

		JRBasePrintEllipse ellipse = new JRBasePrintEllipse(defaultStyleProvider);

		Object actualPage = element.getParameterValue("actualPage");

		ellipse.setKey("" + eqaKey + "-" + actualPage);
		ellipse.setX(x);
		ellipse.setY(y);
		ellipse.setHeight(h);
		ellipse.setWidth(w);
		JRPrintText text = new JRBasePrintText(defaultStyleProvider);

		text.setForecolor(Color.RED);
		text.setX(x + 20);
		text.setY(y + 3);
		text.setHeight(10);
		text.setWidth(200);
		text.setText(eqaKey + " x=" + x + " y=" + y);

		try {
			xmlExporter.exportElement(ellipse);
			xmlExporter.exportText(text);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao tentar exportar elemento", e);
		}
	}

}
