/**
 * 
 */
package br.com.dlp.jazzomr.event;
 
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.collections.CollectionUtils;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.jsf.IJazzDataProvider;
import br.com.dlp.framework.jsf.JazzDataModel;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.exam.coordinate.IOMRMarkable;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.poc.CopiaArquivo;
import br.com.dlp.jazzomr.poc.JazzOMRImageParser;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.DissertativeVO;
import br.com.dlp.jazzomr.results.CriterionDetailDTO;
import br.com.dlp.jazzomr.results.EProcessingState;
import br.com.dlp.jazzomr.results.PayloadVO;

/**
 * 
 * @author darcio
 */
@Scope(value = "session")
@Component
public class EventCorrJSFBean extends EventJSFBean {

	private static final long serialVersionUID = -1335050643413747690L;
	
	/**
	 * Instância de JazzDataModel para paginação
	 */
	private JazzDataModel<PayloadVO> payloadDataModel;
	private JazzDataModel<CriterionDetailDTO> criterionDataModel;
	
	private CriterionDetailDTO criterionDetailDTO;
	
	
	private Collection<SelectItem> payloadOpcoesQtdLinhas;
	private Integer payloadLinhasPorPagina=10;
	private Long payloadLinhasTotais=null;

	private OrderCorrectionE orderCorrectionE = OrderCorrectionE.QUESTION;
	
	@Resource(name="visitorMapping")
	private Map<String, Object> chainedFileWalkers;
	
	@Autowired
	private JazzOMRImageParser imageParser;
	
	protected Long screenImgHeight=900l;
	
	
	private EventVO eventVO;
	
	/**
	 * Construtor padrao
	 */
	public EventCorrJSFBean(){}
	
	
	public List<CriterionDetailDTO> getSelectedCriterions(){
		
		CriterionDetailDTO criterionDetailDTO = getCriterionDetailDTO();
		
		List<CriterionDetailDTO> criterions = new ArrayList<CriterionDetailDTO>();
		
		criterions.add(criterionDetailDTO);
		
		return criterions;
		
		
	}
	
	
	public String zoomIn(){
		Long imgH = this.getScreenImgHeight();
		
		imgH = (long) (imgH * 1.1);
		
		setScreenImgHeight(imgH);
		return "";
	}
	
	public String zoomOut(){
		Long imgH = this.getScreenImgHeight();
		
		imgH = (long) (imgH * 0.9);
		
		setScreenImgHeight(imgH);
		
		return "";
		
	}
	
	public List<CriterionDetailDTO> getLista(){
		
		
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		ExtraArgumentsDTO extraArgumentsDTO = new ExtraArgumentsDTO();
		
		EventVO eventVO2 = new EventVO();
		eventVO2.setPK(7l);
		
		List<CriterionDetailDTO> res = eventBusiness.findCriterionDetails(eventVO2, extraArgumentsDTO);
		
		return res;
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public List<SelectItem> getNotas(AbstractCriterionVO abstractCriterionVO){
		
		if(abstractCriterionVO==null || abstractCriterionVO instanceof AlternativeVO){
			return new ArrayList<SelectItem>(0);
		}
		
		DissertativeVO dissertativeVO = (DissertativeVO) abstractCriterionVO;
		
		Integer maxPonto = dissertativeVO.getMaxPonto();
		
		List<SelectItem> notas = new ArrayList<SelectItem>(maxPonto+1);
		
		for (int n=0; n<=maxPonto; n++) {
			SelectItem si = new SelectItem(n, ""+n);
			notas.add(si);
		}
		
		return notas;
		
	}

	/**
	 * Action
	 * @param event
	 */
  public void actionHandleFileUpload(UploadEvent event) {  
  	List<UploadItem> files = event.getUploadItems();
  	processUploadFiles(files);
    addMessage(FacesMessage.SEVERITY_INFO, "Imagens carregadas com sucesso!", "");
  }


  /**
   * Inicia processamento de cada arquivo enviado via upload
   * @param files
   */
	public void processUploadFiles(List<UploadItem> files) {
		for (UploadItem uploadItem : files) {
			
			//transforma qualquer arquivo que vier (img, zip, gzip, pdf, word) numa colecao de imagens para processamento
  		readImagesFromFile(uploadItem);
  		
		}
	}


	/**
	 * Transforma qualquer arquivo que vier (img, zip, gzip, pdf, word) numa colecao de imagens para processamento
	 * @param uploadedFile
	 * @return
	 */
	protected void readImagesFromFile(UploadItem uploadItem) {
		
		String contentType = uploadItem.getContentType();
		
		
		IImageFileWalk walk = (IImageFileWalk) chainedFileWalkers.get(contentType);

		if(walk==null){
			addMessage(FacesMessage.SEVERITY_INFO, "Tipo de arquivo não reconhecido!", "");
			addMessage(FacesMessage.SEVERITY_INFO, "Tipos aceitos: imagens, PDF e ZIP.", "");
			
		}

		IImageParserVisitor visitor= new IImageParserVisitor() {
			/*
			public void visit(byte[] bs) {

				ByteArrayInputStream bais = new ByteArrayInputStream(bs);
				BufferedImage image;
				
				try {
					image = ImageIO.read(bais);
				} catch (IOException e) {
					throw new JazzRuntimeException("Erro ao tentar ler stream de origem da imagem",e);
				}
				
				this.visit(image);
			}
			*/

			public void visit(BufferedImage bi) {
				processImageFile(bi);
			}
		};
		
		byte[] data = getData(uploadItem);
		walk.processFile(data,visitor);
		
		log.debug(uploadItem.getContentType());
	}


	/**
	 * Recupera o array de bytes vindo do arquivo uploaded
	 * 
	 * @param uploadItem
	 * @return
	 */
	protected byte[] getData(UploadItem uploadItem) {
		
		if(!uploadItem.isTempFile()){
			return uploadItem.getData();
		}else{
			
			File file = uploadItem.getFile();
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				CopiaArquivo.copy(fis, baos, 2048);

				baos.flush();
				baos.close();
				fis.close();
				
				return baos.toByteArray();
				
			} catch (FileNotFoundException e) {
				throw new JazzRuntimeException("Erro ao tentar processar arquivo vindo por upload",e);
			} catch (IOException e) {
				throw new JazzRuntimeException("Erro ao tentar processar arquivo vindo por upload",e);
			}finally{
				
				file.delete();
			}
		}
	}


	protected void processImageFile(BufferedImage bi) {
		
		
		ImageDocVO imageDocVO;
		try {
			
			imageDocVO = imageParser.prepareImageProcessing(bi);
		} catch (JazzOMRException e1) {
			throw new JazzRuntimeException("Erro ao tentar carregar imagem inicial para processamento",e1);
		}
		try {
			//tenta parsear possivel imagem de exame
			imageParser.parseImage(imageDocVO);

			//sucesso
			imageParser.updatePayloadVO(imageDocVO);
			
		} catch (JazzOMRException e) {
			//erro
			imageParser.updatePayloadVO(imageDocVO, e);
			
		}
	}
	

	@SuppressWarnings("unchecked")
	public void detailEvent(EventVO eventVO) {
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		List<EventVO> eventVOs = eventBusiness.findEventResults(eventVO);
		
		EventVO eventoRetorno = null;
		
		if(!CollectionUtils.isEmpty(eventVOs)){
		
			eventoRetorno = eventVOs.get(0);
			
		}
		
		
		setTmpVO(eventoRetorno);
	}

	/**
	 * Escreve arquivo de exames, referente ao evento escolhido, em HttpServletResponse
	 * @param eventVO
	 */
	public void downloadResults(EventVO eventVO) {

		ParticipationHelper participationHelper = getParticipationHelper();
		
		if( CollectionUtils.isEmpty(eventVO.getParticipations())){
			addMessage(FacesMessage.SEVERITY_INFO, "Não há participações registradas para este evento por enquanto.", "");
			return;
		}

		Map<String, Object> parameters = mountResultReportParams(eventVO);

		JasperReport jasperReport = participationHelper.getJasperReport("/reports/RelatorioNotas.jasper");

		JasperPrint jasperPrint = participationHelper.fillReport(parameters, jasperReport);


		byte[] pdfReport = participationHelper.exportToPDF(jasperPrint);
		
		
		ByteArrayInputStream bais = new ByteArrayInputStream(pdfReport);
		
		HttpServletResponse response = prepareHttpResponse(eventVO, pdfReport, "Resultados_{0}_{1}.pdf");

		writePDFResponse(bais, response);

	}


	public Map<String, Object> mountResultReportParams(EventVO eventVO) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("events_pks", eventVO.getPK());
		
		Integer countTotal=0;
		
		Map<EProcessingState,Integer> countMap = eventVO.getPercentCountMap();
		for (EProcessingState eProcessingState : countMap.keySet()) {
			Integer count = countMap.get(eProcessingState);
			parameters.put(eProcessingState.toString(),new Float(count));
			
			countTotal = countTotal + count;
		}
		
		
		parameters.put("TOTAL_PAGINAS", new Float(countTotal));
		
		return parameters;
	}
	
	
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo. Pesquisa e retorna lista de EventVO com paginação e ordenação de dados.
	 * 
	 * Traz lista de eventos com as participacoes e payloas: permite observar estado do processamento dos payloads
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<EventVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		//executa a primeira pesquisa apenas no primeiro nivel de relacionamentos, para fazer valer a paginacao de resultados. 
		//Trazer relacionamentos ....ToMany agora deixaria a paginacao inconsistente
		List<EventVO> resultados = eventBusiness.findEventVO(getDescription(), getDtFimFrom(), getDtFimTo(), getDtInicioFrom(), getDtInicioTo(), 
				getDtIncFrom(), getDtIncTo(), getDtAltFrom(), getDtAltTo(), getStatus(), extraArgumentsDTO);		
				
		//agora complementa a lista retornada com os fetchprofiles necessarios para trazer as colecoes de participacoes e payloads
		//List<EventVO> eventosEncontrados = eventBusiness.findBeansByBeans(resultados,"event_participations","participations_payloadVOs");
		
		List<EventVO> eventosEncontrados = eventBusiness.findEvents(resultados, extraArgumentsDTO);
				
		
		
		//bingo: lista paginada corretamente, mas com os fetchprofiles desejados
		return eventosEncontrados;
				
	}


	
	
	public JazzDataModel<PayloadVO> getPayloadDataModel() {
		
		
		if(this.payloadDataModel==null){
			this.payloadDataModel = new JazzDataModel<PayloadVO>(new IJazzDataProvider<PayloadVO>() {
				
				public List<PayloadVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
					
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					String[] fetchProfile = new String[]{""};
					
					List<PayloadVO> resultados = eventBusiness.findPayloadVO(extraArgumentsDTO, fetchProfile);
					
					return resultados;
				}

				public boolean isRowAvailable(Serializable currentPk) {
					return currentPk!=null;
				}

				public Long cachedRowCount() {
					
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					return eventBusiness.countNotProcessedPayloadVO();
					
				}
			});
		}
		
		return this.payloadDataModel;
	}


	public Collection<SelectItem> getPayloadOpcoesQtdLinhas() {
		return payloadOpcoesQtdLinhas;
	}


	public Integer getPayloadLinhasPorPagina() {
		return payloadLinhasPorPagina;
	}


	public Long getPayloadLinhasTotais() {
		return payloadLinhasTotais;
	}


	public void setPayloadDataModel(JazzDataModel<PayloadVO> payloadDataModel) {
		this.payloadDataModel = payloadDataModel;
	}


	public void setPayloadOpcoesQtdLinhas(Collection<SelectItem> payloadOpcoesQtdLinhas) {
		this.payloadOpcoesQtdLinhas = payloadOpcoesQtdLinhas;
	}


	public void setPayloadLinhasPorPagina(Integer payloadLinhasPorPagina) {
		this.payloadLinhasPorPagina = payloadLinhasPorPagina;
	}


	public void setPayloadLinhasTotais(Long payloadLinhasTotais) {
		this.payloadLinhasTotais = payloadLinhasTotais;
	}

	
	public Long getScreenImgHeight() {
		return screenImgHeight;
	}


	public void setScreenImgHeight(Long examImageSize) {
		this.screenImgHeight = examImageSize;
	}

	public OrderCorrectionE getOrderCorrectionE() {
		return orderCorrectionE;
	}


	public void setOrderCorrectionE(OrderCorrectionE orderCorrectionE) {
		this.orderCorrectionE = orderCorrectionE;
	}

	
	
	
	
	
	
	

	public JazzDataModel<CriterionDetailDTO> getCriterionDataModel() {
		
		if(this.criterionDataModel==null){
			this.criterionDataModel = new JazzDataModel<CriterionDetailDTO>(new IJazzDataProvider<CriterionDetailDTO>() {
				
				@SuppressWarnings("unchecked")
				public List<CriterionDetailDTO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
					
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					Collection<CriterionDetailDTO> criterionDetails = eventBusiness.findCriterionDetails(getEventVO(), extraArgumentsDTO);

					List<CriterionDetailDTO> criterionDetailDTOs = new ArrayList<CriterionDetailDTO>(criterionDetails);
					
					return criterionDetailDTOs;
				}

				
				public boolean isRowAvailable(Serializable currentPk) {
					return currentPk!=null;
				}
				
				
				public Long cachedRowCount() {
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					ExtraArgumentsDTO extraArgumentsDTO=new ExtraArgumentsDTO();

					Collection<CriterionDetailDTO> criterionDetails = eventBusiness.findCriterionDetails(getEventVO(), extraArgumentsDTO);

					List<CriterionDetailDTO> criterionDetailDTOs = new ArrayList<CriterionDetailDTO>(criterionDetails);
					
					long size = (long) criterionDetailDTOs.size();
					return size;					
					
					
				}
				
			});
		}
		
		return criterionDataModel;
	}


	public void setCriterionDataModel(JazzDataModel<CriterionDetailDTO> criterionDataModel) {
		this.criterionDataModel = criterionDataModel;
	}


	public EventVO getEventVO() {
		return eventVO;
	}


	public void setEventVO(EventVO eventVO) {
		this.eventVO = eventVO;
	}
	
	
	
	public String zubalele(){
		System.out.println("teste");
		return "";
	}
	
	
	/**
	 * 
	 * @param criterionDetailDTO
	 * @return
	 */
	public String iniciarRoteiroCorrecao(CriterionDetailDTO criterionDetailDTO){
		
		if(criterionDetailDTO.getCriterionResultVO()==null){
			CriterionResultVO criterionResultVO = new CriterionResultVO();
			criterionResultVO.setParticipationVO(criterionDetailDTO.getParticipationVO());
			criterionResultVO.setCriterionCoordinateVO(criterionDetailDTO.getCriterionCoordinateVO());
		}
		
		if(!criterionDetailDTO.isPayloadFetched()){
			fetchPayload(criterionDetailDTO);
		}
		
		setCriterionDetailDTO(criterionDetailDTO);
		
		return "";
	}


	/**
	 * @param criterionDetailDTO
	 */
	protected void fetchPayload(CriterionDetailDTO criterionDetailDTO) {
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		PayloadVO payloadVO = criterionDetailDTO.getPayloadVO();
		
		payloadVO = eventBusiness.fetchPayload(payloadVO);
		
		criterionDetailDTO.setPayloadVO(payloadVO);
	}
	
	public void testeSubmit(){
		System.out.println("teste submit #################**************************************************************************************#####");
		
	}

	/**
	 * 
	 * @param step
	 * @return
	 */
	public String stepRoteiro(Integer step){
		CriterionDetailDTO criterionDetailDTO = getCriterionDetailDTO();
		
		CriterionDetailDTO nextCriterion = null;
		if(criterionDetailDTO!=null){
			nextCriterion = stepCriterionList(step,criterionDetailDTO);
			saveCriterionDetail(criterionDetailDTO);
		}
		
		
		if(nextCriterion!=null){
			iniciarRoteiroCorrecao(nextCriterion);
		}
		
		
		return "";
	}

	
	public String saveActualCriterion(){
		CriterionDetailDTO criterionDetailDTO = getCriterionDetailDTO();
		saveCriterionDetail(criterionDetailDTO);
		
		return "";
	}
	
	
	/**
	 * 
	 * @param criterionDetailDTO
	 */
	protected void saveCriterionDetail(CriterionDetailDTO criterionDetailDTO) {
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		CriterionResultVO criterionResultVO =  criterionDetailDTO.getCriterionResultVO();
		
		
		eventBusiness.saveQuestionResultVO(criterionResultVO);
		

	}


	/**
	 * 
	 * @param step
	 * @param criterionDetailDTO
	 * @return
	 */
	public CriterionDetailDTO stepCriterionList(Integer step, CriterionDetailDTO criterionDetailDTO){
		
		List<CriterionDetailDTO> resultados = getCriterionDataModel().getResultados();
		if( CollectionUtils.isEmpty(resultados)){
			return null;
		}else if(resultados.size()==1){
			return criterionDetailDTO;
		}
		
		CriterionDetailDTO retCrit = null;
		
		Integer nextIdx = resultados.indexOf(criterionDetailDTO)+step;
		
		if(nextIdx>=0 && resultados.size()>nextIdx){
			retCrit = resultados.get(nextIdx);
			
		}else {
			retCrit = resultados.get(0);
			
		}
		
		return retCrit;
	}
	

	public CriterionDetailDTO getCriterionDetailDTO() {
		return criterionDetailDTO;
	}


	public void setCriterionDetailDTO(CriterionDetailDTO criterionDetailDTO) {
		this.criterionDetailDTO = criterionDetailDTO;
	}

}
