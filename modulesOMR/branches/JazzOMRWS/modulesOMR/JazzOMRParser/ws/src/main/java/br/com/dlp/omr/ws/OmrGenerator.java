package br.com.dlp.omr.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.annotations.DataBinding;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationHelperWS;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.parser.JazzOMRImageParser;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.omr.ws.payload.CorrectionPayloadVO;
import br.com.dlp.omr.ws.payload.GenerationPayloadVO;
import br.com.dlp.omr.ws.payload.PageImageVO;

@Component
@DataBinding(AegisDatabinding.class)
public class OmrGenerator implements IOmrGenerator {

	@Resource
	WebServiceContext wsContext;
  
	
	@Autowired
	ParticipationHelperWS participationHelperWS;
	
	@Autowired
	private JazzOMRImageParser jazzOMRImageParser;
	
	public OmrGenerator() {
	}
	

	/**
	 * 
	 */
	public java.util.List<QuestionResultVO> processImages(CorrectionPayloadVO processImagesPayloadVO) {
		
		List<PageImageVO> pageImages = processImagesPayloadVO.getPageImages();
		EventVO eventVO  = processImagesPayloadVO.getEventVO();
		List<QuestionResultVO> fullResults = new ArrayList<QuestionResultVO>();
		
		for (PageImageVO pageImageVO : pageImages) {
			try {
				
				ImageDocVO imageDocVO = new ImageDocVO(pageImageVO.getPageImage());
				List<QuestionResultVO> questionResults = jazzOMRImageParser.parseImage(imageDocVO, eventVO);
				
				fullResults.addAll(questionResults);
				
			} catch (JazzOMRException e) {
				throw new JazzRuntimeException("Erro ao tentar processar as imagens de páginas e evento informados!"+e.getMessage(),e);
			}
		}
		
		return fullResults ;
		
	}
	
	/**
	 * 
	 */
	public EventVO generateEvent(GenerationPayloadVO genPayloadVO){

		ExamVO examVO = genPayloadVO.getExamVO();
		List<PessoaVO> pessoas = genPayloadVO.getPessoas();
		Integer qtdVariacoes = genPayloadVO.getQtdVariacoes();
		String eventName = genPayloadVO.getEventName();
		
		JasperReport templateProvaJR = getTemplateProva(genPayloadVO);
		
		EventVO eventVO;
		try {
			eventVO =participationHelperWS.processParticipations(eventName,examVO, pessoas, qtdVariacoes, templateProvaJR);
		} catch (JazzBusinessException e) {
			throw new JazzRuntimeException("Erro ao tentar gerar prova!",e);
		}
		
		return eventVO;
	}
	
	
	
	private JasperReport getTemplateProva(GenerationPayloadVO genPayloadVO) {
		
		JasperReport jasperReport = genPayloadVO.getModeloProva();
		
		if(jasperReport==null){
			DataHandler dHandler = genPayloadVO.getModeloProvaDH();
			
			if(dHandler!=null){
				try {
					jasperReport = (JasperReport) JRLoader.loadObject(dHandler.getInputStream());
				} catch (JRException e) {
					throw new JazzRuntimeException("Erro ao tentar carregar os bytes enviados como JasperReport. Um arquivo .jasper é esperado! "+e.getMessage(),e);
				} catch (IOException e) {
					throw new JazzRuntimeException("Erro ao tentar carregar os bytes enviados como JasperReport. Um arquivo .jasper é esperado! "+e.getMessage(),e);
				} catch (Exception e) {
					throw new JazzRuntimeException("Erro ao tentar carregar os bytes enviados como JasperReport. Um arquivo .jasper é esperado! "+e.getMessage(),e);
				}
			}
		}
		
		
		if(jasperReport==null){
			
			Log.error("nenhum template de relatório foi informado. Utilizando template default /reports/ExamReportDSP2.jrxml ");
			
			InputStream stream = this.getClass().getResourceAsStream("/reports/ExamReportDSP2.jrxml");

			try {
				jasperReport = JasperCompileManager.compileReport(stream);
			} catch (JRException e) {
				throw new JazzRuntimeException("Nenhum template de relatório foi informado. Envie um arquivo em base64 no campo 'modeloProva', ou um arquvo em anexo no padrão MTOM, no campo modeloProvaDH!");
			}

		}
		
		return jasperReport;
	}



	
}
