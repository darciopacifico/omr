package br.com.dlp.jazzomr.parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.exam.ExamOMRMetadataVO;
import br.com.dlp.jazzomr.exam.coordinate.OMRMark;
import br.com.dlp.jazzomr.exceptions.JazzOMRRuntimeException;

/**
 * Handler para SAX Parser.
 * Le as coordenadas das alternativas e criterios de questções
 * @author darcio
 *
 */
public class ExamInstanceXMLHandlerWS extends DefaultHandler {
	
	private static final long serialVersionUID = 4996383560623589800L;

         
	//                                                   part  exa qn  pe    cr    ev   pg  TY            
	private static final String ACO_REGEX_KEY = "aco_Key-(.*)-((.*-.*-(.*))-(.*))-(.*)-(.*)-(.*)";
	private static final Integer GRUPO_PAGINA = 7;
	private static final Integer GRUPO_CRIT_TYPE = 8;
	private static final Integer GRUPO_CHAVE = 2;
	private static final Integer GRUPO_EXAM_VARIANT = 6;
	private static final Integer GRUPO_PERGUNTA = 4;
	private static final Integer GRUPO_CRITERION = 5;

	
	private static final String OMR_MARK_REGEX_KEY = "OMRMark-(.+)-(.+)";
	private static final String PAGEIDENTIFIER_REGEX_KEY = "(\\d+)-(\\d+)";
	
	
	private static final Pattern pageIdentifier_pattern = Pattern.compile(PAGEIDENTIFIER_REGEX_KEY);
	private static final Pattern aco_pattern = Pattern.compile(ACO_REGEX_KEY);
	private static final Pattern omr_mark_pattern = Pattern.compile(OMR_MARK_REGEX_KEY);
	private static final Logger log = LoggerFactory.getLogger(ExamInstanceXMLHandlerWS.class);


	
	private boolean waitForTextContentElement=false;
	
	private RelatorioVO relatorioVO;

	private StringBuffer strPage= new StringBuffer();
	
	
	private boolean colectPageIdentifier;

	private EventVO eventVO;
	
	
	
	/**
	 * Implementação de startElement para leitura de XML
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		if(this.waitForTextContentElement && qName.equals("textContent")){
			this.colectPageIdentifier=true;
		}
		
		if(qName!=null && qName.equals("reportElement")){
			
			String keyValue = attributes.getValue("key");
			
			if(keyValue!=null){

				//System.out.println(keyValue);
				
				if(keyValue.equals("pageIdentifier")){
					waitForTextContentElement=true;
					super.startElement(uri, localName, qName, attributes);
					return;
				}
				
				Matcher aco_matcher = aco_pattern.matcher(keyValue);
				Matcher omr_mark_matcher=null;
				
				if(!getRelatorioVO().isAllCornerOMRMarksfound()){
					//apenas para evitar criar o matcher para sempre mesmo que todas as marcas tenham sido encontradas
					omr_mark_matcher = omr_mark_pattern.matcher(keyValue);
				}
				
				if(aco_matcher.matches()){
					//o valor do atributo key bate com o padrao esperado para uma coordenada de bullet
					fillAlternativeCoordinate(attributes, aco_matcher);
					
				}else if(isFindCornerOMRMarks(omr_mark_matcher)){
					//o valor do atributo key bate com o padrao esperado para uma marca omr de quina *E* nem todas as necessárias foram encontradas
					fillOMRMarkCoordinate(attributes, omr_mark_matcher);
				}
			}
			
			if(log.isDebugEnabled()){
				log.debug(keyValue);
			}
			
		}
		super.startElement(uri, localName, qName, attributes);
	}
	
	
	/**
	 * Apenas para evitar um erro comum quando nao ha conexao com a internet.
	 * Simplesmente para evitar java.net.UnknownHostException para encontrar o dtd quando a internet está fora.
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
		//simplesmente para evitar java.net.UnknownHostException para encontrar o dtd quando a internet está fora.
		return new org.xml.sax.InputSource(new java.io.StringReader("")); 
	}
	
	/**
	 * Construtor padrao
	 * @param eventVO 
	 * @param coordinateMap 
	 * @param coordinateMap 
	 * @param hibernateTemplate2 
	 * @param mapAlternativeCoords 
	 * @param mapQuestionCoords 
	 * @param imageDocVO
	 */
	public ExamInstanceXMLHandlerWS(EventVO eventVO) {
		this.relatorioVO=eventVO.getRelatorioVO();
		this.eventVO = eventVO;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(this.colectPageIdentifier){
			strPage.append(ch,start,length);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equals("textContent")){
			
			if(this.colectPageIdentifier){
				
				@SuppressWarnings("unused")
				String pageIdentifier = strPage.toString();
				Matcher matcher = pageIdentifier_pattern.matcher(pageIdentifier);
				
				if(matcher.matches()){
					this.strPage=new StringBuffer();
					this.waitForTextContentElement=false;
					this.colectPageIdentifier=false;
				}
				
			}

		}
		
		super.endElement(uri, localName, qName);
	}
	


	/**
	 * Testa se ainda será necessário processar as coordenadas das marcas OMR (discos pretos nas quinas dos relatórios), ou se todos as coordenadas necessárias já foram encontradas
	 * @param omr_mark_matcher
	 * @return
	 */
	protected boolean isFindCornerOMRMarks(Matcher omr_mark_matcher) {
		return !getRelatorioVO().isAllCornerOMRMarksfound() && omr_mark_matcher.matches();
	}


	/**
	 * Preenche as coordenadas e a página de um bullet de alternativa de questão 
	 * @param attributes
	 * @param aco_matcher 
	 * @param pagina 
	 * @param matcher
	 * @return
	 */
	protected void fillAlternativeCoordinate(Attributes attributes, Matcher aco_matcher) {

		
		String exVar = aco_matcher.group(GRUPO_EXAM_VARIANT);
		String page = aco_matcher.group(GRUPO_PAGINA);
		String perg = aco_matcher.group(GRUPO_PERGUNTA);

		
		String chaveExVarPage = exVar+"-"+page;
		Set<String> pergs = getPergsByExVarPage(chaveExVarPage);
		pergs.add(perg);
		
		
		OMRMark omrMark = createOMRMarkable(attributes, new Integer(page), aco_matcher);
		
		String chavePergExVarPage = perg+"-"+exVar+"-"+page;
		Set<OMRMark> omrMarks = getOmrMarksByPergExVarPage(chavePergExVarPage);
		omrMarks.add(omrMark);
		
		
		
	}


	protected Set<OMRMark> getOmrMarksByPergExVarPage(String chavePergExVarPage) {
		Set<OMRMark> omrMarks = eventVO.getMapOMRMarksByPergExamVarPage().get(chavePergExVarPage);
		
		if(omrMarks == null){
			omrMarks = new HashSet<OMRMark>();
			eventVO.getMapOMRMarksByPergExamVarPage().put(chavePergExVarPage,omrMarks);
		}
		return omrMarks;
	}


	protected Set<String> getPergsByExVarPage(String chaveExVarPage) {
		Set<String> pergs = eventVO.getMapPerguntasByExamVarPage().get(chaveExVarPage);
		
		if(pergs==null){
			pergs = new HashSet<String>();
			eventVO.getMapPerguntasByExamVarPage().put(chaveExVarPage, pergs);
		}
		return pergs;
	}




	protected OMRMark createOMRMarkable(Attributes attributes, Integer intPagina,  Matcher aco_matcher) {
		OMRMark coordVO = new OMRMark();
		
		atribuiCoordenadasEPagina(attributes, coordVO,intPagina, aco_matcher);
			
		//registra em quantas páginas este criterio apareceu. Na correção será necessário levantar todas as páginas.
		coordVO.getPaginas().add(intPagina);
		return coordVO;
	}





	protected Integer getExamVar(Matcher aco_matcher) {
		String examVar = aco_matcher.group(GRUPO_EXAM_VARIANT);
		if(!NumberUtils.isDigits(examVar))throw new JazzOMRRuntimeException("Erro ao tentar preencher coordenadas! O valor de examVar "+examVar+" nao é numerico!");
		Integer lExamVar = new Integer(examVar);
		return lExamVar;
	}


	protected Integer getPagina(Matcher aco_matcher) {
		String pagina = aco_matcher.group(GRUPO_PAGINA);
		if(!NumberUtils.isDigits(pagina))throw new JazzOMRRuntimeException("Erro ao tentar preencher coordenadas! O valor de pagina "+pagina+" nao é numerico!");
		Integer intPagina = new Integer(pagina);
		return intPagina;
	}

	/**
	 * Atribui as coordenadas e a página encontradas nos attributes do XML do relatório ao objeto CriterionCoordinateVO. 
	 * 
	 * @param attributes
	 * @param pkAco
	 * @param omrMark
	 * @param intPagina 
	 * @param chave 
	 */
	protected void atribuiCoordenadasEPagina(Attributes attributes, OMRMark omrMark, Integer intPagina, Matcher aco_matcher) {
		//se o criterio possuir múltiplas páginas, eu devo registrar as coordenadas da primeira página onde aparece o critério, pois é onde está o enunciado do mesmo.
		
		
		
		String strX = attributes.getValue("x");
		String strY = attributes.getValue("y");
		String strW = attributes.getValue("width");
		String strH = attributes.getValue("height");
		
		Integer x=Integer.parseInt(strX);
		Integer y=Integer.parseInt(strY);
		Integer w=Integer.parseInt(strW);
		Integer h=Integer.parseInt(strH);
		
		omrMark.setH(h);
		omrMark.setW(w);
		omrMark.setX(x);
		omrMark.setY(y);
		
		String chave = aco_matcher.group(GRUPO_CHAVE);
		String critType = aco_matcher.group(GRUPO_CRIT_TYPE);
		String pergunta = aco_matcher.group(GRUPO_PERGUNTA);
		String criterion = aco_matcher.group(GRUPO_CRITERION);
		
		Long longPergunta = new Long(pergunta);

		omrMark.setCriterion(criterion);		
		omrMark.setChave(chave);
		omrMark.setPergunta(longPergunta);
		omrMark.setCritType(critType);
		omrMark.setPagina(intPagina);
		
		
	}


	/**
	 * Preenche as coordenadas dos discos de referência encontrados nos cantos das provas.
	 * @param attributes
	 * @param matcher
	 * @return
	 */
	protected void fillOMRMarkCoordinate(Attributes attributes, Matcher matcher) {
		
		//simples leitura dos atributos
		String pos1 = matcher.group(1);
		String pos2 = matcher.group(2);
		
		String omrMarksKey = pos1+"-"+pos2;
		//Verificando (mais uma vez) se a coordenada esta registrada
		ExamOMRMetadataVO examOMRMetadataVO = this.getRelatorioVO().getExamOMRMetadataVO().get(omrMarksKey);
		
		if(examOMRMetadataVO==null){
			
			String strX = attributes.getValue("x");
			String strY = attributes.getValue("y");
			String strW = attributes.getValue("width");
			String strH = attributes.getValue("height");
			
			Integer x=Integer.parseInt(strX);
			Integer y=Integer.parseInt(strY);
			Integer w=Integer.parseInt(strW);
			Integer h=Integer.parseInt(strH);
			
			//caso a coordenada ainda nao esteja registrada, cria-se uma nova com os valores encontrados no XML
			Double xCenter = x.doubleValue()+(w/2);
			Double yCenter = y.doubleValue()+(h/2);
			
			examOMRMetadataVO = new ExamOMRMetadataVO();
			
			examOMRMetadataVO.setX(xCenter);
			examOMRMetadataVO.setY(yCenter);
			examOMRMetadataVO.setOmrKey(omrMarksKey);
			
			//atualiza mapa de coordenadas do exame
			this.getRelatorioVO().getExamOMRMetadataVO().put(omrMarksKey, examOMRMetadataVO);
		}
		
		
	}

	public RelatorioVO getRelatorioVO() {
		return relatorioVO;
	}

	public void setRelatorioVO(RelatorioVO relatorioVO) {
		this.relatorioVO = relatorioVO;
	}


	public EventVO getEventVO() {
		return eventVO;
	}


	public void setEventVO(EventVO eventVO) {
		this.eventVO = eventVO;
	}



}
