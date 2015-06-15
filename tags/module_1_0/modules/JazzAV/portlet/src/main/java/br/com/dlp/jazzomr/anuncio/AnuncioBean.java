/**
 * 
 */
package br.com.dlp.jazzomr.anuncio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.jazzav.anuncio.AnuncioVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * Managed bean responsável pelo controle e conversacao da edicao do anuncio
 * 
 * @author darcio
 */
@ManagedBean
@SessionScoped
@Scope(value="session")
@Component
public class AnuncioBean {

	public AnuncioVO anuncioVO = new AnuncioVO();

	
	public AnuncioVO getAnuncioVO() {
		return anuncioVO;
	}

	public void setAnuncioVO(AnuncioVO anuncioVO) {
		this.anuncioVO = anuncioVO;
	}

	public void paint(OutputStream out, Object data) throws IOException, TemplateException {

		InputStream svgStream = SVGMainTests.class.getResourceAsStream("/br/com/dlp/jazzav/modeloanuncio/teste.svg");
		String modeloAnuncio = IOUtils.toString(svgStream);
		
		
		String strSVG = modeloAnuncio.replaceAll("&quot;", "\"");
				
		StringReader reader = new StringReader(strSVG);
		
		Configuration cfg = new Configuration();
		
		
		
		Template template = new Template("teste", reader, cfg,"UTF-16");
		
		
		Map rootMap = new HashMap();
		
		rootMap.put("anuncio", anuncioVO);
		
		StringWriter writer = new StringWriter();
		
		template.process(rootMap, writer);
		
		
		
		
		svgStream = IOUtils.toInputStream(writer.getBuffer().toString());
		
		
		Transcoder transcoder = new PNGTranscoder(); 
		
		Map hintMap = new HashMap();
		
		hintMap.put(PNGTranscoder.KEY_WIDTH, 400f);
		hintMap.put(PNGTranscoder.KEY_HEIGHT, 300f);
		
		transcoder.setTranscodingHints(hintMap);
		
		TranscoderInput in = new TranscoderInput(svgStream);
		TranscoderOutput tout = new TranscoderOutput(out);
		
		try {
			transcoder.transcode(in, tout);
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.flush();
		out.close();
	}

}
