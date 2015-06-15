/**
 * 
 */
package br.com.dlp.jazzomr.results;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

import org.apache.cxf.aegis.Context;
import org.apache.cxf.aegis.DatabindingException;
import org.apache.cxf.aegis.type.basic.Base64Type;
import org.apache.cxf.aegis.xml.MessageReader;
import org.apache.cxf.aegis.xml.MessageWriter;
import org.apache.cxf.common.util.XMLSchemaQNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author darciopa
 */
public class JazzJasperTypeCreator extends Base64Type {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private String formatName = "pdf";

	@Override
	public QName getSchemaType() {
		return XMLSchemaQNames.XSD_BASE64;
	}

	public Object readObject(MessageReader reader, Context context) throws DatabindingException {
		byte[] bytes = (byte[]) super.readObject(reader, context);

		if (bytes == null || bytes.length == 0) {
			log.warn("Nao foi enviado nenhum byte para criacao de JasperReport!");
			return null;
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		JasperReport jasperReport;

		try {
			jasperReport = (JasperReport) JRLoader.loadObject(bais);
		} catch (JRException e) {
			throw new DatabindingException("Erro ao tentar carregar relatorio enviado como template de prova!", e);
		}

		return jasperReport;

	}

	public void writeObject(Object object, MessageWriter writer, Context context) throws DatabindingException {

		if (object == null || !(object instanceof JasperReport))
			throw new DatabindingException("Erro ao tentar converter JasperReport para array de bytes!");

		JasperReport jasperReport = (JasperReport) object;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			JRSaver.saveObject(jasperReport, baos);
		} catch (JRException e) {
			throw new DatabindingException("Erro ao tentar salvar arquivo jasper para byte array!", e);
		}

		super.writeObject(baos.toByteArray(), writer, context);  
	}

}
