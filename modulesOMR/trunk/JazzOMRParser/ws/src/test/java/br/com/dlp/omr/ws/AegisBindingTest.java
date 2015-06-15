package br.com.dlp.omr.ws;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.aegis.AegisContext;
import org.apache.cxf.aegis.AegisReader;
import org.apache.cxf.aegis.AegisWriter;
import org.apache.cxf.aegis.type.AegisType;
import org.apache.cxf.aegis.type.DefaultTypeMapping;
import org.apache.cxf.aegis.type.TypeCreationOptions;
import org.apache.cxf.aegis.type.TypeCreator;
import org.apache.cxf.aegis.type.TypeMapping;
import org.apache.cxf.common.util.SOAPConstants;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.com.dlp.jazzomr.event.EventVO;

@Test
public class AegisBindingTest {

	private AegisContext context;
	private XMLOutputFactory outputFactory;
	private XMLInputFactory inputFactory;
    
	@BeforeTest
	public void setUp(){
		context = getAegisContext();
		outputFactory = XMLOutputFactory.newInstance();
		inputFactory = XMLInputFactory.newInstance();
	}
	
	@Test
	public void testBinding() throws Exception{
	
		EventVO eventVO = new EventVO();
        String xmlFileName = "saidaEvento.xml";
        OutputStream baosXml = new FileOutputStream(xmlFileName);
        InputStream fisXML = new FileInputStream(xmlFileName);

		eventVoToXML(context, outputFactory, eventVO, baosXml);
        
		EventVO evenVOLido = xmlToEventVO(context, inputFactory, fisXML);
        
        System.out.println(evenVOLido);
        
	}

	private void eventVoToXML(AegisContext context,
			XMLOutputFactory outputFactory, EventVO eventVO,
			OutputStream baosXml) throws XMLStreamException, Exception,
			IOException {
		AegisWriter<XMLStreamWriter> writer = context.createXMLStreamWriter();
		XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(baosXml);
		QName qname = new QName("urn:jazzomr:event", "event");
		boolean ignoreNull = true;
		AegisType arg4 = context.getTypeMapping().getType(EventVO.class);
		writer.write(eventVO, qname, ignoreNull, xmlWriter, arg4);
        xmlWriter.close();
        baosXml.close();
	}

	private EventVO xmlToEventVO(AegisContext context,
			XMLInputFactory inputFactory, InputStream fisXML)
			throws XMLStreamException, Exception {
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(fisXML);
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
