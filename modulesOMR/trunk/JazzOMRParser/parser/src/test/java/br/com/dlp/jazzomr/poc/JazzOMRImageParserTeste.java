package br.com.dlp.jazzomr.poc;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.cxf.aegis.AegisContext;
import org.apache.cxf.aegis.AegisReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.parser.JazzOMRImageParser;

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
	
	/**
	 * @throws Exception 
	 * @throws XMLStreamException 
	 */
	@Test
	public void testDirectory() throws FactoryConfigurationError, XMLStreamException, Exception{
		
		EventVO eventVO = readEvento("/eventoTeste/eventoSaida.xml");
		

		List<InputStream> images = new ArrayList<InputStream>();
		
		images.add(JazzOMRImageParser.class.getResourceAsStream("/eventoTeste/pagina-0.jpeg"));
		images.add(JazzOMRImageParser.class.getResourceAsStream("/eventoTeste/pagina-1.jpeg"));
		images.add(JazzOMRImageParser.class.getResourceAsStream("/eventoTeste/pagina-2.jpeg"));
		images.add(JazzOMRImageParser.class.getResourceAsStream("/eventoTeste/pagina-3.jpeg"));
		
		List<QuestionResultVO> resultsTotal = jazzOMRImageParser.parseImages(eventVO, images);
		
		long now = System.currentTimeMillis();
		
		
		for (QuestionResultVO questionResultVO : resultsTotal) {

			System.out.println(
					" Questao:"+questionResultVO.getQuestionPK()+
					" Participacao:"+questionResultVO.getParticipation()+
					" Pessoa:"+questionResultVO.getPessoaPK()+
					" ExamVar:"+questionResultVO.getExamVar()
					);

			Set<CriterionResultVO> critRes = questionResultVO.getCriterionResultVOs();

			for (CriterionResultVO criterionResultVO : critRes) {

				System.out.println("\t" + criterionResultVO.getCriterion() + " tipo=" + criterionResultVO.getCritType() + " checked = " + criterionResultVO.getChecked());

			}

		}
		
		System.out.println("tempo total "+(System.currentTimeMillis()-now));
		
	}

	@Test
	public void testFileWalker(){
		Object obj = chainedFileWalkers.get("application/zip");
		System.out.println(obj);
	}
	

	private EventVO readEvento(String xmlEvento) throws FactoryConfigurationError, XMLStreamException, Exception {
		AegisContext aegisContext = initializeAegisContext();
		InputStream fileInputStream = JazzOMRImageParserTeste.class.getResourceAsStream(xmlEvento);
		EventVO eventVO = deserializeXML(aegisContext , fileInputStream );
		// TODO Auto-generated method stub
		return eventVO;
	}
	
	
	protected EventVO deserializeXML(AegisContext aegisContext, InputStream fileInputStream) throws FactoryConfigurationError, FileNotFoundException, XMLStreamException, Exception {
		AegisReader<XMLStreamReader> reader = aegisContext.createXMLStreamReader();
		
		XMLInputFactory inputFactory = XMLInputFactory.newFactory();
		
		InputStream inputStream =  fileInputStream;
		XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputStream);
		
		
		Object readedObj = reader.read(xmlReader);
		
		EventVO eventLido = (EventVO) readedObj;
		
		System.out.println("escrevento evento original");
		
		System.out.println("escrevento evento Lido de XML");
		return eventLido;
	}
	

	/*
	protected void logCoordinates(EventVO eventVO) {
		Map<Integer, Map<String, OMRMark>> mapExVar = eventVO.getMapExVarCoordinates();
		Set<Integer> keys = mapExVar.keySet();

		for (Integer examVar : keys) {
			Map<String, OMRMark> mapMark = mapExVar.get(examVar);
			System.out.println(examVar);

			Set<String> chaves = mapMark.keySet();

			for (String chave : chaves) {
				OMRMark omrMark = mapMark.get(chave);
				System.out.println("\t " + chave + " = " + omrMark);
			}
		}
	}*/
	

	protected AegisContext initializeAegisContext() {
		AegisContext aegisContext = new AegisContext();
		
		aegisContext.setWriteXsiTypes(true);
		
		Set<Type> rootClasses = new HashSet<Type>();
		rootClasses .add(EventVO.class);
		//aegisContext.setRootClasses(rootClasses );
		
		aegisContext.initialize();
		return aegisContext;
	}
	
}