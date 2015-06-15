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
 * @author darcio
 */
public class JazzMessageFormat {
	private Log log = LogFactory.getLog(JazzMessageFormat.class);
	
	private String baseName;
	private ResourceBundle resourceBundle;

	
	/**
	 * Construtor que recebe local do arquivo de mensagens
	 * @param baseName
	 */
	public JazzMessageFormat(String baseName) {
		this.baseName = baseName;
		this.resourceBundle = ResourceBundle.getBundle(baseName);
	}
	
	
	/**
	 * Formatador simples
	 * @param msgKey
	 * @param obj
	 * @return
	 */
	public String format(String msgKey, Object... obj){
		String msg;
		try{
			msg = resourceBundle.getString(msgKey);
		}catch (MissingResourceException e) {
			log.error(MessageFormat.format("A mensagem {0} não foi encontrada no bundle {1} !", msgKey,baseName));
			return "msg!"+msgKey+"!["+obj+"]!";
			
		}
		
		return MessageFormat.format(msg, obj);
	}
}
