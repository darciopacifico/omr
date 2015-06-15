package br.com.dlp.jazzomr.poc;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.exam.ExamOMRMetadataVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;

/**
 * Handler para SAX Parser.
 * Le as coordenadas das alternativas e criterios de questões
 * @author darcio
 *
 */
public class ExamInstanceXMLHandler extends DefaultHandler {
	
	private static final long serialVersionUID = 4996383560623589800L;

	private static final int GROUP_UM = 1;
	private static final int GROUP_DOIS = 2;
	private static final int GROUP_TRES = 3;
	private static final int GROUP_QCO = 1;

	private Map<Long, CriterionCoordinateVO> mapAlternativeCoords;

	private boolean waitForTextContentElement=false;
	
	private RelatorioVO relatorioVO;

	private StringBuffer strPage= new StringBuffer();
	
	private Integer paginaAtual=0;
	
	private boolean colectPageIdentifier;
	
	private static final String ACO_REGEX_KEY = "aco_Key-(.+)-(\\d+)-(\\d+)";
	private static final String OMR_MARK_REGEX_KEY = "OMRMark-(.+)-(.+)";
	private static final String PAGEIDENTIFIER_REGEX_KEY = "(\\d+)-(\\d+)";
	
	
	private static final Pattern pageIdentifier_pattern = Pattern.compile(PAGEIDENTIFIER_REGEX_KEY);
	private static final Pattern aco_pattern = Pattern.compile(ACO_REGEX_KEY);
	private static final Pattern omr_mark_pattern = Pattern.compile(OMR_MARK_REGEX_KEY);
	private static final Logger log = LoggerFactory.getLogger(ExamInstanceXMLHandler.class);
	
	
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
	 * @param hibernateTemplate2 
	 * @param mapAlternativeCoords 
	 * @param mapQuestionCoords 
	 * @param imageDocVO
	 */
	public ExamInstanceXMLHandler(RelatorioVO relatorioVO, Map<Long, CriterionCoordinateVO> mapAlternativeCoords) {
		this.relatorioVO=relatorioVO;
		this.mapAlternativeCoords = mapAlternativeCoords;
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
					String strPagina = matcher.group(GROUP_DOIS);
					this.paginaAtual = new Integer(strPagina);

					//ao termino do processamento, a ultima pagina setada sera o igual a quantidade de paginas
					getRelatorioVO().setTotalPages(this.paginaAtual);
					
					this.strPage=new StringBuffer();
					this.waitForTextContentElement=false;
					this.colectPageIdentifier=false;
				}
				
			}

		}
		
		super.endElement(uri, localName, qName);
	}
	
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		if(this.waitForTextContentElement && qName.equals("textContent")){
			this.colectPageIdentifier=true;
		}
		
		if(qName!=null && qName.equals("reportElement")){
			
			String key = attributes.getValue("key");
			
			if(key!=null){

				
				if(key.equals("pageIdentifier")){
					waitForTextContentElement=true;
					super.startElement(uri, localName, qName, attributes);
					return;
				}
				
				Matcher aco_matcher = aco_pattern.matcher(key);
				Matcher omr_mark_matcher=null;
				
				if(!getRelatorioVO().isAllCornerOMRMarksfound()){
					//apenas para evitar criar o matcher para sempre mesmo que todas as marcas tenham sido encontradas
					omr_mark_matcher = omr_mark_pattern.matcher(key);
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
				log.debug(key);
			}
			
		}
		super.startElement(uri, localName, qName, attributes);
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
	 * @param attributes
	 * @param matcher
	 * @return
	 */
	protected void fillAlternativeCoordinate(Attributes attributes, Matcher matcher) {
		String strAco = matcher.group(ExamInstanceXMLHandler.GROUP_DOIS);
		Long pkAco = Long.parseLong(strAco);

		CriterionCoordinateVO acoVO = mapAlternativeCoords.get(pkAco);
		
		if(acoVO==null){
			String msg = "A coordenada do criterio "+pkAco+", encontrado no xml de prova, nao foi encontrada no mapa de coordenadas ("+mapAlternativeCoords+"). Verifique se a query do relatorio está correta!";
			log.error(msg);
			throw new JazzRuntimeException(msg);
		}

		if(!isCoordenadasPreenchidas(acoVO)){
			//se o criterio possuir múltiplas páginas, eu devo registrar as coordenadas da primeira página onde aparece o critério, pois é onde está o enunciado do mesmo. 
		
			String strX = attributes.getValue("x");
			String strY = attributes.getValue("y");
			String strW = attributes.getValue("width");
			String strH = attributes.getValue("height");
			
			Integer x=Integer.parseInt(strX);
			Integer y=Integer.parseInt(strY);
			Integer w=Integer.parseInt(strW);
			Integer h=Integer.parseInt(strH);
			
			acoVO.setPK(pkAco);
			acoVO.setH(h);
			acoVO.setW(w);
			acoVO.setX(x);
			acoVO.setY(y);
		
			acoVO.setPagina(this.paginaAtual);
		}
		acoVO.getPaginas().add(this.paginaAtual);
		
	}

	/**
	 * Testa se as coordenadas deste critério já forma preenchidas 
	 * @param acoVO
	 * @return
	 */
	protected boolean isCoordenadasPreenchidas(CriterionCoordinateVO acoVO) {
		return acoVO.getPagina()!=null;
	}
	

	/**
	 * @param attributes
	 * @param matcher
	 * @return
	 */
	protected void fillOMRMarkCoordinate(Attributes attributes, Matcher matcher) {
		
		//simples leitura dos atributos
		String pos1 = matcher.group(1);
		String pos2 = matcher.group(2);
		
		String omrMarksKey = pos1+"-"+pos2;

		String strX = attributes.getValue("x");
		String strY = attributes.getValue("y");
		String strW = attributes.getValue("width");
		String strH = attributes.getValue("height");
		
		Integer x=Integer.parseInt(strX);
		Integer y=Integer.parseInt(strY);
		Integer w=Integer.parseInt(strW);
		Integer h=Integer.parseInt(strH);

		//Verificando (mais uma vez) se a coordenada esta registrada
		ExamOMRMetadataVO examOMRMetadataVO = this.getRelatorioVO().getExamOMRMetadataVO().get(omrMarksKey);
		
		if(examOMRMetadataVO==null){
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



}
