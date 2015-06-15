package br.com.dlp.omr.ws;

import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;

import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.omr.ws.vo.GenerationPayloadVO;
import br.com.dlp.omr.ws.vo.GenerationReturnVO;
import br.com.dlp.omr.ws.vo.PageImageVO;


@DataBinding(AegisDatabinding.class)
@WebService
public interface IOmrGenerator {
	
	public GenerationReturnVO gerarProvas(
			@WebParam(name="parametros")
			GenerationPayloadVO parametros);
 

	public List<QuestionResultVO> corrigirProvas(
			@XmlMimeType("application/xml")
			@WebParam(name="arquivoGabarito")
			DataHandler arquivoGabarito, 
			
			@WebParam(name="paginasDigitalizadas")
			List<PageImageVO> paginasDigitalizadas);

}
