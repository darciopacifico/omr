package br.com.dlp.omr.ws;

import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.omr.ws.payload.GenerationPayloadVO;
import br.com.dlp.omr.ws.payload.CorrectionPayloadVO;


@DataBinding(AegisDatabinding.class)
@WebService
public interface IOmrGenerator {
	public EventVO generateEvent(GenerationPayloadVO generationPayloadVO);
 
	public List<QuestionResultVO> processImages(CorrectionPayloadVO processImagesPayloadVO );
	
}
