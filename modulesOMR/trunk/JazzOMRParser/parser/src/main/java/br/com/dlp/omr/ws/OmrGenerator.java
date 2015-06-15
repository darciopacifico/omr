package br.com.dlp.omr.ws;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.cxf.aegis.AegisContext;
import org.apache.cxf.aegis.AegisReader;
import org.apache.cxf.aegis.AegisWriter;
import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.aegis.type.AegisType;
import org.apache.cxf.aegis.type.DefaultTypeMapping;
import org.apache.cxf.aegis.type.TypeCreationOptions;
import org.apache.cxf.aegis.type.TypeCreator;
import org.apache.cxf.aegis.type.TypeMapping;
import org.apache.cxf.annotations.DataBinding;
import org.apache.cxf.common.util.SOAPConstants;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.jazzomr.event.EventProcessDTO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationHelperWS;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.exceptions.JazzOMRRuntimeException;
import br.com.dlp.jazzomr.parser.JazzOMRImageParser;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.omr.ws.vo.GenerationPayloadVO;
import br.com.dlp.omr.ws.vo.GenerationReturnVO;
import br.com.dlp.omr.ws.vo.PageImageVO;

@Component
@DataBinding(AegisDatabinding.class)
public class OmrGenerator implements IOmrGenerator {

	private static final String MIME_GABARITO = " application/zip";

	@Resource
	WebServiceContext wsContext;
  
	@Autowired
	ParticipationHelperWS participationHelperWS;
	
	@Autowired
	private JazzOMRImageParser jazzOMRImageParser;
	
	private AegisContext context;
	private XMLOutputFactory outputFactory;
	private XMLInputFactory inputFactory;
	
	/**
	 * Inicializa os contextos de processamento de XML, input e output factories
	 */
	public OmrGenerator() {
		context = getAegisContext();
		outputFactory = XMLOutputFactory.newInstance();
		inputFactory = XMLInputFactory.newInstance();
	}
	
	
	
	
	/**
	 * 
	 */
	public java.util.List<QuestionResultVO> corrigirProvas(
			@XmlMimeType("application/xml")
			@WebParam
			DataHandler arquivoGabarito,
			@WebParam(name="pageImages")
			List<PageImageVO> pageImages) {
		
		EventVO eventVO  = getEventoVO(arquivoGabarito);
		
		List<BufferedImage> images = new ArrayList<BufferedImage>(pageImages.size());
			
		for (PageImageVO pageImageVO2 : pageImages) {
			images.add(pageImageVO2.getPageImage());
		}
		
		
		List<QuestionResultVO> questionResults;
		try {
			questionResults = jazzOMRImageParser.parseImage(images, eventVO);
		} catch (JazzOMRException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar processar as imagens enviadas! "+e.getMessage(),e);
		}
		
		return questionResults ;
		
	}
	



	private EventVO getEventoVO(DataHandler dh) {
		EventVO eventVO;
		try {
			InputStream is = dh.getInputStream();
			
			eventVO = xmlToEventVO(is);
			
		} catch (Exception e) {
			throw new JazzOMRRuntimeException("Erro ao tentar ler xml de gabarito! "+e.getMessage(),e);
		}
		return eventVO;
	}


	/**
	 * 
	 */
	public GenerationReturnVO gerarProvas(GenerationPayloadVO genPayloadVO){

		String eventName 				= genPayloadVO.getEventName();
		ExamVO examVO 					= genPayloadVO.getExamVO();
		List<PessoaVO> pessoas 			= genPayloadVO.getPessoas();
		Integer qtdVariacoes			= genPayloadVO.getQtdVariacoes();
		JasperReport templateProvaJR 	= getTemplateProva(genPayloadVO);
		
		
		
		GenerationReturnVO generationReturnVO; 
		
			
			EventProcessDTO eventDTO = participationHelperWS.processParticipations(eventName,examVO, pessoas, qtdVariacoes, templateProvaJR);
			
			generationReturnVO = generateReturn(eventDTO);
		
			
		return generationReturnVO;
	}
	
	
	
	
	
	/**
	 * 
	 * @param eventDTO
	 * @return
	 */
	private GenerationReturnVO generateReturn(EventProcessDTO eventDTO) {
		
		JasperPrint jasperPrint = eventDTO.getJasperPrint();
		EventVO eventVO = eventDTO.getEventVO();
		
		DataHandler dataHandlerProva = exportToPDF(jasperPrint);
		
		DataHandler dataHandlerEvento = exportToXML(eventVO);
		
		GenerationReturnVO generationReturnVO = new GenerationReturnVO();
		
		generationReturnVO.setProvaFile(dataHandlerProva);
		generationReturnVO.setArquivoGabarito(dataHandlerEvento);
		

		return generationReturnVO;
	}


	/**
	 * 
	 * @param eventVO
	 * @return
	 */
	private DataHandler exportToXML(EventVO eventVO) {

		ByteArrayOutputStream baosXml = new ByteArrayOutputStream();
		
		ZipEntry entry = new ZipEntry("gabarito.xml");
		
		ZipOutputStream zos = new ZipOutputStream(baosXml);
		
		
		
		
		try {
			zos.putNextEntry(entry);
			
			eventVoToXML(eventVO, zos);
			zos.close();
			zos.flush();
			baosXml.close();
		} catch (Exception e) {
			throw new JazzOMRRuntimeException("Erro ao tentar converter objeto evento para XML! "+e.getMessage(),e);
		}
		
		
		DataSource ds = new ByteArrayDataSource(baosXml.toByteArray(),MIME_GABARITO);
		
		DataHandler dataHandler = new DataHandler(ds);
		
		return dataHandler;
	}


	/**
	 * Exporta jasperPrint para PDF e coloca o arquivo num dataHandler, para ser atachado numa resposta de webService
	 * 
	 * @param jasperPrint
	 * @return
	 */
	private DataHandler exportToPDF(JasperPrint jasperPrint) {
		JRPdfExporter exporter = new JRPdfExporter();
		
		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
		
		ZipOutputStream zos = new ZipOutputStream(baosPDF);
		ZipEntry entry = new ZipEntry("prova.pdf");
		
		
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, zos);
		
		try {
			zos.putNextEntry(entry);
			exporter.exportReport();
			zos.flush();
			zos.close();
			baosPDF.close();
			
		} catch (JRException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar exportar jasperPrint para PDF! "+e.getMessage(),e);
		} catch (IOException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar exportar jasperPrint para PDF! "+e.getMessage(),e);
		}
		
		byte[] byteArray = baosPDF.toByteArray();
		
		DataSource dataSource = new ByteArrayDataSource(byteArray, "application/zip");
		
		
		DataHandler dataHandlerProva = new DataHandler(dataSource );
		
		
		return dataHandlerProva;
	}


	private JasperReport getTemplateProva(GenerationPayloadVO genPayloadVO) {
		JasperReport jasperReport=null;
		/*
		JasperReport jasperReport = genPayloadVO.getModeloProva();
		
		if(jasperReport==null){
			DataHandler dHandler = genPayloadVO.getModeloProvaDH();
			
			if(dHandler!=null){
				try {
					jasperReport = (JasperReport) JRLoader.loadObject(dHandler.getInputStream());
				} catch (Exception e) {
					throw new JazzOMRRuntimeException("Erro ao tentar carregar os bytes enviados como JasperReport. Um arquivo .jasper é esperado! "+e.getMessage(),e);
				}
			}
		}
		*/
		
		if(jasperReport==null){
			
			Log.error("nenhum template de relatório foi informado. Utilizando template default /reports/ExamReportDSP2.jrxml ");
			
			InputStream stream = this.getClass().getResourceAsStream("/reports/ExamReportDSP2.jrxml");

			try {
				jasperReport = JasperCompileManager.compileReport(stream);
			} catch (JRException e) {
				throw new JazzOMRRuntimeException("Nenhum template de relatório foi informado. Envie um arquivo em base64 no campo 'modeloProva', ou um arquvo em anexo no padrão MTOM, no campo modeloProvaDH!");
			}
		}
		
		return jasperReport;
	}

	

    
	
	


	private void eventVoToXML(EventVO eventVO, OutputStream baosXml) throws Exception {
		
		AegisWriter<XMLStreamWriter> writer = context.createXMLStreamWriter();
		XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(baosXml);
		QName qname = new QName("urn:jazzomr:event", "event");
		boolean ignoreNull = true;
		AegisType arg4 = context.getTypeMapping().getType(EventVO.class);
		writer.write(eventVO, qname, ignoreNull, xmlWriter, arg4);
        xmlWriter.close();
        baosXml.close();
	}

	private EventVO xmlToEventVO(InputStream fisXML)throws XMLStreamException, Exception {
		ZipInputStream zis = new ZipInputStream(fisXML);
		
		//apenas para posicionar no primeiro entry 
		zis.getNextEntry();
		
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(zis);
        
		AegisReader<XMLStreamReader> reader = context.createXMLStreamReader();
        
        EventVO evenVOLido = (EventVO) reader.read(streamReader);
        
		return evenVOLido;
	}

	private AegisContext getAegisContext() {
		AegisContext context = new AegisContext();
		
		context.setWriteXsiTypes(true);
        context.setReadXsiTypes(true);
        
        TypeCreationOptions tco = new TypeCreationOptions();
        tco.setQualifyElements(false);
        
        Set<java.lang.reflect.Type> rootClasses = new HashSet<java.lang.reflect.Type>();
        Type genericType = EventVO.class;
		rootClasses.add(genericType );
        context.setTypeCreationOptions(tco);
        context.setRootClasses(rootClasses);
        
        //TypeMapping baseMapping = DefaultTypeMapping.createSoap11TypeMapping(false, false, false);
        
        TypeMapping baseMapping = DefaultTypeMapping.createDefaultTypeMapping(false, false, false);
        
        DefaultTypeMapping mapping = new DefaultTypeMapping(SOAPConstants.XSD, baseMapping);
        TypeCreator stockTypeCreator = context.createTypeCreator();
 
        mapping.setTypeCreator(stockTypeCreator);
        context.setTypeMapping(mapping);
        context.initialize();
		return context;
	}
	
	
}
