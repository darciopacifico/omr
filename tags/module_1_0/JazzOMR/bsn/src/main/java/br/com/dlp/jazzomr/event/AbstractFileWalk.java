/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author darcio
 */
/**
 * @author darcio
 *
 */
public abstract class AbstractFileWalk implements IImageFileWalk {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="visitorMapping")
	protected Map<String, Object> chainedFileWalkers;
	
	
	public AbstractFileWalk() {
	}
	
	/**
	 * Encontra um IImageFileWalk para processar os bytes de entryContent
	 * 
	 * @param entryContent
	 * @return
	 * @throws IOException
	 */
	protected IImageFileWalk getImageFileWalk(ByteArrayOutputStream entryContent) throws IOException {
		String mimeType = getMimeType(entryContent);
		IImageFileWalk fileWalk = (IImageFileWalk) chainedFileWalkers.get(mimeType);
		
		if(fileWalk==null){
			log.warn("Erro ao processar arquivo. Nao foi encontrada uma implementacao de IImageFileWalk para o mime "+mimeType);
		}
		
		return fileWalk;
	}


	/**
	 * Lê os dados do inputstream e os coloca num byte array 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	protected ByteArrayOutputStream readEntry(InputStream stream) throws IOException {
		
    // create a buffer to improve copy performance later.
    byte[] buffer = new byte[2048];
    
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try
		{
		    int len = 0;
		    while ((len = stream.read(buffer)) > 0)
		    {
		        output.write(buffer, 0, len);
		    }
		    
		}
		finally
		{
		    if(output!=null) output.close();
		}
		return output;
	}


	/**
	 * Detecta o mimetype do arquivo contido no ByteArrayOutputStream
	 * @param output
	 * @return
	 * @throws IOException
	 */
	protected String getMimeType(ByteArrayOutputStream output) throws IOException {
		byte[] byteArray = output.toByteArray();
		String mimeType = getMimeType(byteArray);
		return mimeType;
	}


	/**
	 * Detecta o mimetype do arquivo contido no array de bytes
	 * 
	 * @param byteArray
	 * @return
	 * @throws IOException
	 */
	protected String getMimeType(byte[] byteArray) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		String mimeType =  URLConnection.guessContentTypeFromStream(byteArrayInputStream);
		return mimeType;
	}
	
	
	/**
	 * Apenas fecha o InputStream, ignorando qualquer eventual erro
	 * 
	 * @param stream
	 */
	protected void closeStream(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			//do nothing
		}
	}

	
	/**
	 * @return
	 */
	public Map<String, Object> getChainedFileWalkers() {
		return chainedFileWalkers;
	}

	/**
	 * @param visitors
	 */
	public void setChainedFileWalkers(Map<String, Object> visitors) {
		this.chainedFileWalkers = visitors;
	}


}


