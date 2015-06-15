/**
 * 
 */
package br.com.dlp.jazzomr.results;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.namespace.QName;

import org.apache.cxf.aegis.Context;
import org.apache.cxf.aegis.DatabindingException;
import org.apache.cxf.aegis.type.basic.Base64Type;
import org.apache.cxf.aegis.xml.MessageReader;
import org.apache.cxf.aegis.xml.MessageWriter;
import org.apache.cxf.common.util.XMLSchemaQNames;

import br.com.dlp.jazzomr.exceptions.JazzOMRRuntimeException;

/**
 * @author darciopa
 *
 */
public class JazzImageTypeCreator extends Base64Type {

	private String formatName="gif"; 

	
	@Override 
	public QName getSchemaType() {
		// TODO Auto-generated method stub
		return XMLSchemaQNames.XSD_BASE64;
	}
	
	
    public Object readObject(MessageReader reader, Context context) throws DatabindingException{
    	
    	byte[] bytes = (byte[]) super.readObject(reader, context);
    	
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		BufferedImage bufferedImage;
		
		try {
			bufferedImage = ImageIO.read(bais);
		} catch (IOException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar ler array de bytes como bufferedImage!",e);
		}
		
		return bufferedImage;
    	
    }

    public void writeObject(Object object, MessageWriter writer, Context context) throws DatabindingException{
    	
		if(object==null || !(object instanceof BufferedImage))
			throw new JazzOMRRuntimeException("Erro ao tentar converter objeto para formato interno (object="+object+")!");
			
		BufferedImage bi = (BufferedImage) object;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(bi, formatName, baos);
		} catch (IOException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar ler array de bytes a partir de imagem!",e);
		}
			
		byte[] bytes = baos.toByteArray();
    	
    	super.writeObject(bytes, writer, context);
    	
    }
	
}
