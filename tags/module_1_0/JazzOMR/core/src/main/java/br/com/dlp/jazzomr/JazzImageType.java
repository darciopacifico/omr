/**
 * 
 */
package br.com.dlp.jazzomr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Types;

import javax.imageio.ImageIO;

import org.hibernate.type.AbstractLongBinaryType;

import br.com.dlp.framework.exception.JazzRuntimeException;

/**
 * Tipo customizado para armazenamento e recuperacao de imagens em banco de dados
 * @author darcio
 *
 */
public class JazzImageType extends AbstractLongBinaryType {
	private static final long serialVersionUID = 8703578207200100533L;
	public static final String TYPE_NAME = "omrimg";

	private String formatName="gif"; 
	
	/**
	 * omrimg
	 */
	@Override
	public String getName() {
		return JazzImageType.TYPE_NAME;
	}

	/**
	 * @return BufferedImage.class;  
	 */
	public Class getReturnedClass() {
		return BufferedImage.class;
	}

	/**
	 * Espera um array de bytes contendo uma imagem. Tenta ler o array de bytes como imagem
	 */
	protected Object toExternalFormat(byte[] bytes) {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		BufferedImage bufferedImage;
		
		try {
			bufferedImage = ImageIO.read(bais);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar ler array de bytes como bufferedImage!",e);
		}
		
		return bufferedImage;
	}

	/**
	 * Converte objeto (BufferedImage) informado para array de bytes
	 * 
	 */
	protected byte[] toInternalFormat(Object object) {
		
		if(object==null || !(object instanceof BufferedImage))
			throw new JazzRuntimeException("Erro ao tentar converter objeto para formato interno (object="+object+")!");
			
		BufferedImage bi = (BufferedImage) object;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(bi, formatName, baos);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar ler array de bytes a partir de imagem!",e);
		}
			
		return baos.toByteArray();
	}


	/**
	 * Retorna tipo padrao para sql blob
	 */
	@Override
	public int sqlType() {
		return Types.BLOB;
	}
	
}
