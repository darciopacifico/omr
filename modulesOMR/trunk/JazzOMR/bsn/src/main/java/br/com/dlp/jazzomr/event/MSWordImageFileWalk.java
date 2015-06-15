/**
 * 
 */
package br.com.dlp.jazzomr.event;

import org.springframework.stereotype.Component;

/**
 * Navega pela estrutura de um arquivo ZIP recursivamente ate encontrar as imagens contidas no ZIP, entao aciona o processamento das mesmas
 * 
 * @author darcio
 * 
 */
@Component("msWordImageFileWalk")
public class MSWordImageFileWalk extends AbstractFileWalk {

	/**
	  	 (java.lang.String) application/vnd.openxmlformats-officedocument.wordprocessingml.document
	 (java.lang.String) application/msword
	 (java.lang.String) application/pdf
	 (java.lang.String) application/x-tar
	 (java.lang.String) application/x-rar
	 (java.lang.String) image/png
	 (java.lang.String) image/gif
	 (java.lang.String) image/jpeg
	 (java.lang.String) zipedExams.zip
	 (java.lang.String) application/zip
	 */
	
	public MSWordImageFileWalk() {
	}

	/**
	 * Processa os bytes de um arquivo compactado. Visita todos os arquivos contidos no ZIP, detecta o mimetype e aciona um novo IImageFileWalk, ate
	 * chegar aas folhas da estrutura, que são as imagens de exames
	 */
	@Override
	public void processFile(byte[] bytes, IImageParserVisitor visitor) {
		
		//listar as imagens contidas no documento e acionar visitor.visit(...) para cada uma
		
	}

}
