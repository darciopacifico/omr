package br.com.dlp.omr.ws;

import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.xml.bind.annotation.XmlMimeType;

import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.omr.ws.vo.GenerationPayloadVO;
import br.com.dlp.omr.ws.vo.GenerationReturnVO;
import br.com.dlp.omr.ws.vo.PageImageVO;

public interface IOmrGerenatorAsync extends IOmrGenerator {

	public @WebResult(name = "protocoloGeracaoProva") 
	String gerarProvasAsyncRequest(@WebParam(name = "parametrosGeracaoProva") GenerationPayloadVO parametros);

	public @WebResult(name = "resultadoGeracaoProva") 
	GenerationReturnVO gerarProvasAsyncRequest(@WebParam(name = "protocoloGeracaoProva") String protocol);
	
	
	
	

	public @WebResult(name = "protocoloCorrecaoProva") String corrigirProvasAsyncRequest(
			@XmlMimeType("application/xml")
			@WebParam(name="arquivoGabarito")
			DataHandler arquivoGabarito, 
			
			@WebParam(name="paginasDigitalizadas")
			List<PageImageVO> paginasDigitalizadas);
	
	public @WebResult(name = "resultadoCorrecaoProva")  List<QuestionResultVO> corrigirProvas(@WebParam(name = "protocoloCorrecaoProva") String protocoloCorrecao);
	
	
	
}
