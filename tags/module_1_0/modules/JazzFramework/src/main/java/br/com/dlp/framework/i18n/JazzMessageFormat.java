/**
 * 
 */
package br.com.dlp.framework.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Facilitador de internacionalização para mensagens de diálogo
 * 
 * @author darcio
 */
public class JazzMessageFormat {
	private final String baseName;
	
	private final Log log = LogFactory.getLog(JazzMessageFormat.class);
	private final ResourceBundle resourceBundle;
	
	/**
	 * Construtor que recebe local do arquivo de mensagens
	 * 
	 * @param baseName
	 */
	public JazzMessageFormat(final String baseName) {
		this.baseName = baseName;
		resourceBundle = ResourceBundle.getBundle(baseName);
	}
	
	/**
	 * Formatador simples
	 * 
	 * @param msgKey
	 * @param obj
	 * @return
	 */
	public String format(final String msgKey, final Object... obj) {
		String msg;
		try {
			msg = resourceBundle.getString(msgKey);
		} catch (final MissingResourceException e) {
			log.error(MessageFormat.format("A mensagem {0} não foi encontrada no bundle {1} !", msgKey, baseName));
			return "msg!" + msgKey + "![" + obj.toString() + "]!";// NOSONAR
			
		}
		
		return MessageFormat.format(msg, obj);
	}
}
