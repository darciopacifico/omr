/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzRuntimeException;

/**
 * Navega pela estrutura de um arquivo ZIP recursivamente ate encontrar as imagens contidas no ZIP, entao aciona o processamento das mesmas
 * 
 * @author darcio
 * 
 */
@Component("compressedFileWalk")
public class ZIPImageFileWalk extends AbstractFileWalk {

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
	
	public ZIPImageFileWalk() {
	}

	/**
	 * Processa os bytes de um arquivo compactado. Visita todos os arquivos contidos no ZIP, detecta o mimetype e aciona um novo IImageFileWalk, ate
	 * chegar aas folhas da estrutura, que são as imagens de exames
	 */
	@Override
	public void processFile(byte[] bytes, IImageParserVisitor visitor) {

		ZipInputStream zippedStream = new ZipInputStream(new ByteArrayInputStream(bytes));
		
		try {

			ZipEntry zipEntry;
			while ((zipEntry = zippedStream.getNextEntry()) != null) {

				if (!zipEntry.isDirectory()) {

					ByteArrayOutputStream entryContent = readEntry(zippedStream);
					
					//encadeia uma chamada a uma implementacao de IImageFileWalk
					IImageFileWalk fileWalk = getImageFileWalk(entryContent);
					
					if(fileWalk!=null){
						//se foi encontrado um IImageFileWalk para o tipo do arquivo o processo continua
						fileWalk.processFile(entryContent.toByteArray(),visitor);
					}
				}
			}
		} catch (IOException e) {
			
			throw new JazzRuntimeException("Erro ao tentar processar ZipEntry", e);
			
		} finally {
			closeStream(zippedStream);
		}
	}
}
