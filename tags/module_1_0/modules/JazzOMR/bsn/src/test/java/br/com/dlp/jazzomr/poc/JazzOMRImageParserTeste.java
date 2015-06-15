package br.com.dlp.jazzomr.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.exceptions.RefParticleNotFoundException;
import br.com.dlp.jazzomr.omr.ImageDocVO;

/**
 * Testes em parsing de imagens contendo dados OMR
 * @author darcio
 */
@ContextConfiguration(locations = "/ApplicationContext_testng.xml")
@Test
public class JazzOMRImageParserTeste extends AbstractTestNGSpringContextTests {
	
	public static Logger log = LoggerFactory.getLogger(JazzOMRImageParserTeste.class);
	
	@Autowired
	private JazzOMRImageParser jazzOMRImageParser;
	
	
	@Resource(name="visitorMapping")
	private Map<String, Object> chainedFileWalkers;
	
	@Test
	public void testFileWalker(){
		
		Object obj = chainedFileWalkers.get("application/zip");

		
		System.out.println(obj);
		
	}
	

	/**
	 * Parseia imagem contendo dados OMR
	 * 
	 * @param is
	 * @throws JazzOMRException
	 */
	public void parse(InputStream is) throws JazzOMRException {
		ImageDocVO imageDocVO = jazzOMRImageParser.prepareImageProcessing(is);
	
		try {
			jazzOMRImageParser.parseImage(imageDocVO);

			//sucesso
			jazzOMRImageParser.updatePayloadVO(imageDocVO);
			
		} catch (JazzOMRException e) {
			//erro
			jazzOMRImageParser.updatePayloadVO(imageDocVO, e);
			
		}
	}	
	
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws RefParticleNotFoundException
	 * @throws JazzOMRException
	 */
	@Test
	public void testDirectory() throws FileNotFoundException, RefParticleNotFoundException, JazzOMRException{
	
		String strDir = "/home/darcio/workspace/modules/JazzOMR/bsn/digitalizado/";
		File dir = new File(strDir);
		dir.mkdirs();
		
		String[] files = dir.list();
		
		for (String filePath : files) {
			
			System.out.println(filePath);
			long now = System.currentTimeMillis();
			
			String pathname = strDir+filePath;
			
			System.out.println("#######################################################################");
			System.out.println("###: "+pathname);
			
			File file = new File(pathname);
			if(file.isFile()){
				parse(new FileInputStream(file));
			}
			
			System.out.println(pathname + " - " +( System.currentTimeMillis()-now));
			
		}
	}
}