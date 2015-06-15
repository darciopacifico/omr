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
import java.io.OutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.jsf.AbstractDataProvider;
import br.com.dlp.framework.jsf.IJazzDataProvider;
import br.com.dlp.framework.jsf.JazzDataModel;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.poc.CopiaArquivo;
import br.com.dlp.jazzomr.poc.JazzOMRImageParser;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.DissertativeVO;
import br.com.dlp.jazzomr.results.AbstractImageVO;
import br.com.dlp.jazzomr.results.CriterionDetailDTO;
import br.com.dlp.jazzomr.results.EProcessingState;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.dlp.jazzomr.results.PayloadVO;

/**
 * 
 * @author darcio
 */
@Scope(value = "session")
@Component
public class EventProcJSFBean extends EventJSFBean {
	
	

	private static final long serialVersionUID = -1335050643413747690L;
	
	protected BufferedImage notSendedImg = null;

	public EventProcJSFBean getEumesmoporra(){
		return this;
	}
	
	/**
	 * Instância de JazzDataModel para paginação
	 */
	private JazzDataModel<PayloadVO> payloadDataModel;
	private JazzDataModel<CriterionDetailDTO> criterionDataModel;

	private CriterionDetailDTO criterionDetailDTO=new CriterionDetailDTO();
	
	private Collection<SelectItem> payloadOpcoesQtdLinhas;
	private Integer payloadLinhasPorPagina=10;
	private Long payloadLinhasTotais=null;
	
	private PayloadVO selPayloadVO;
	private ParticipationVO selParticipationVO;
	
	
	@Resource(name="visitorMapping")
	private Map<String, Object> chainedFileWalkers;
	
	@Autowired
	private JazzOMRImageParser imageParser;

	


	protected Map<Long, CriterionCoordinateVO> mapCriterionCoordinateVO = new HashMap<Long, CriterionCoordinateVO>(40);

	
	protected Long screenImgHeight=900l;
	
	
	/**
	 * Construtor padrao
	 */
	public EventProcJSFBean(){
		
		System.out.println(this.criterionDetailDTO);
		
	}
	
	

	/**
	 * 
	 * @param step
	 * @return
	 */
	public String stepRoteiro(Long step){
		String stepRoteiro = "";
		if(step!=null){
			stepRoteiro = stepRoteiro(step.intValue());
		}
		
		return stepRoteiro;
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
	 * @param criterionDetailDTO TODO: CACHEAR AS PÁGINAS CARREGADAS NO BEAN DE EVENT PROC
	 */
	protected void fetchPayload(CriterionDetailDTO criterionDetailDTO) {
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		ParticipationVO participationVO = criterionDetailDTO.getParticipationVO();

		List<Integer> paginas = eventBusiness.fetchPaginas(criterionDetailDTO.getCriterionCoordinateVO());
		
		criterionDetailDTO.getCriterionCoordinateVO().setPaginas(paginas);
		
	}
	

	public JazzDataModel<CriterionDetailDTO> getCriterionDataModel() {
		
		if(this.criterionDataModel==null){
			this.criterionDataModel = new JazzDataModel<CriterionDetailDTO>(new AbstractDataProvider<CriterionDetailDTO>() {
				
				@SuppressWarnings("unchecked")
				public List<CriterionDetailDTO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
					
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					Collection<CriterionDetailDTO> criterionDetails = eventBusiness.findCriterionDetails(getTmpVO(), extraArgumentsDTO);

					List<CriterionDetailDTO> criterionDetailDTOs = new ArrayList<CriterionDetailDTO>(criterionDetails);
					
					return criterionDetailDTOs;
				}

				
				public boolean isRowAvailable(Serializable currentPk) {
					return currentPk!=null;
				}
				
				
				public Long cachedRowCount() {
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					ExtraArgumentsDTO extraArgumentsDTO=new ExtraArgumentsDTO();

					Collection<CriterionDetailDTO> criterionDetails = eventBusiness.findCriterionDetails(getTmpVO(), extraArgumentsDTO);

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
	
	
/**
	 * Recupera arvore de arquivos e processos
	 * @return
	 */
	public TreeNode<IBaseVO<? extends Serializable>> getTreeNode(){
		return null;
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
	 * Seleciona resultado de questao a ser detalhado
	 * @param questionDetailDTO
	 * @return
	 */
	public String detailQuestion(CriterionDetailDTO questionDetailDTO){
		return "";
	}


	
	/**
	 * 
	 * @param questionDetailDTO
	 * @param criterionCoordinateVO
	 * @return
	 */
	public String setSelectedCriterion(CriterionDetailDTO questionDetailDTO , CriterionCoordinateVO criterionCoordinateVO){
		
		return "";
	}
	
	
	public String qqrImagem(){
		
		
		List<PayloadVO> results = getPayloadDataModel().getResultados();
		
		PayloadVO payloadVO = results.get(0);
		
		setSelPayloadVO(payloadVO);
		payloadVO = getSelPayloadVO();
		
		return "";
		
		
	}
	
	/**
	 * Retorna stream content contendo imagem da página e participação informadas por request param
	 * @return
	 */
	public StreamedContent streamExamImg() {
		
		if(!isAutorized()){
			//apenas não retorna imagem nenhuma
			return null;
		}
		
		String strPagina = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pagina");
		String strPartPK = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("partPK");
		
		if(StringUtils.isBlank(strPagina) || StringUtils.isBlank(strPartPK)){
			return null;
		}
		
		Integer pagina = Integer.parseInt(strPagina);
		Long partPK = Long.parseLong(strPartPK);
		
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		PayloadVO payloadVO = eventBusiness.findPayloadVO(partPK, pagina);
		

		BufferedImage image;
		if(payloadVO!=null && payloadVO.getImageVO()!=null){
			image = payloadVO.getImageVO().getImage();
		}else{
			image = getNotSendedImg();
		}
		
		return toStreamedContent(image);
		
	}



	/**
	 * Retorna imagem padrão para quando a imagem da participação/pagina não for encontrada
	 * 
	 * @param image
	 * @return
	 */
	protected BufferedImage getNotSendedImg() {
		
		if(this.notSendedImg==null){
			InputStream inputStream = this.getClass().getResourceAsStream("/br/com/dlp/jazzomr/results/imgNaoCarregada.jpg");
			
			try { 
				this.notSendedImg =  ImageIO.read(inputStream);
			} catch (IOException e) {
				log.warn("Erro ao tentar carregar imagem padrao (imgNaoCarregada.jpg)!",e);
			}
		}
		
		return this.notSendedImg;
		
	}
	
	/**
	 * Transforma imagens contidas em BufferedImagens em StreamedContent, para uso de p:GraphicImage do primefaces.
	 * @param bi
	 * @return
	 */
	protected StreamedContent toStreamedContent(BufferedImage bi) {
		StreamedContent streamedContent;
		DefaultStreamedContent content = new DefaultStreamedContent();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "gif", baos);
		} catch (IOException e1) {
			throw new JazzRuntimeException("Erro ao tentar escrever imagem de saida!", e1);
		}

		byte[] imgBytes = baos.toByteArray();

		streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(imgBytes), "image/jpg", "xxx.jpg");
		return streamedContent;
	}


	
  /**
   * Escreve imagem do payload no outputStream. Isto exibira a imagem da tela em EventImageProcessCRUD.xhtml
   * @param outputStream
   * @param payloadVO
   * @throws IOException
   */
  public StreamedContent drawPayloadImage(PayloadVO payloadVO) throws IOException {
  	
  	if(payloadVO==null){
  		//return null;
  	}
  	
  	
  	DefaultStreamedContent streamedContent =  null;
  	
  	if(payloadVO!=null){
	  	
	  	List<ImageVO> images = payloadVO.getImageVOs();
	  	
	  	if(CollectionUtils.isNotEmpty(images)){
	
	  		AbstractImageVO imageVO = images.get(0);
	  		//streamedContent.se

	  		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  		ImageIO.write(imageVO.getImage(), "gif", baos);
	  		
	  		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	  		streamedContent = new DefaultStreamedContent(bais,"image/gif","payload_"+payloadVO.getPK()+"_"+payloadVO.getProcessingState()+".gif");

	  	}
  	}
  	  
  	//setSelPayloadVO(null);
  	
  	return streamedContent;
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
		
		//TODO PREVER DIFERENTES MIMES ENVIADOS PELOS BROWSERS EX: PNG ENVIADO PELO IE NAO FOI RECONHECIDO
		IImageFileWalk walk = (IImageFileWalk) chainedFileWalkers.get(contentType);
		
		if(walk==null){
			addMessage(FacesMessage.SEVERITY_INFO, "Tipo de arquivo não reconhecido!", "");
			addMessage(FacesMessage.SEVERITY_INFO, "Tipos aceitos: imagens, PDF e ZIP.", "");

			addMessage(FacesMessage.SEVERITY_INFO, "Será efetuada uma tentativa de reconhecer o arquivo como imagem.", "");

			walk = (IImageFileWalk) chainedFileWalkers.get("image/gif");
			
		}

		IImageParserVisitor visitor= new IImageParserVisitor() {
			public void visit(BufferedImage bufferedImage) {
				processImageFile(bufferedImage);
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
	

	/**
	 * Consulta detalhes do evento selecionado e disponibiliza o evento cheio neste bean
	 * @param eventVO
	 */
	public void detailEvent(EventVO eventVO) {
		
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		
		
		getCriterionDataModel().getJazzDataProvider().invalidateRowCountCache();
		
		
		List<EventVO> eventVOs = eventBusiness.findEventResults(eventVO);
		
		EventVO eventoRetorno = null;
		
		if(!CollectionUtils.isEmpty(eventVOs)){
			eventoRetorno = eventVOs.get(0);
		}
		
		setTmpVO(eventoRetorno);
	}
	
	
	/**
	 * Pesquisa detalhes sobre a participação escolhida: questoes, coordenadas etc...
	 * @param participationVO
	 */
	public void detailParticipation(ParticipationVO participationVO) {
		setSelParticipationVO(participationVO);
	}


	/**
	 * @param questionDetails
	 */
	protected void setSelectedCriterion(List<CriterionDetailDTO> questionDetails) {
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
		
		String fileName = MessageFormat.format("Resultados_Evento_{0}.pdf", eventVO.getPK());
		
		HttpServletResponse response = prepareHttpResponse(pdfReport, fileName, "application/pdf");

		writeFileDownloadResponse(bais, response);

	}


	/**
	 * 
	 * @param eventVO
	 * @return
	 */
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
	 * Método utilizado pelo primefaces para exibir imagens na tela
	 * @param outputStream
	 * @param object
	 * @throws IOException
	 */
	public void paint(OutputStream outputStream, Object object) throws IOException {
  	File file = (File) object;
  	
  	InputStream inputStream = new FileInputStream(file);
		
		CopiaArquivo.copy(inputStream, outputStream, 1024);
	}	
  
  
  /**
   * 
   * @return
   */
  public long getTimeStamp(){
    return System.currentTimeMillis();
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
		
		Boolean hasParticipations = true;
		//executa a primeira pesquisa apenas no primeiro nivel de relacionamentos, para fazer valer a paginacao de resultados. 
		//Trazer relacionamentos ....ToMany agora deixaria a paginacao inconsistente
		List<EventVO> resultados = eventBusiness.findEventVO(getDescription(), getDtFimFrom(), getDtFimTo(), getDtInicioFrom(), getDtInicioTo(), 
				getDtIncFrom(), getDtIncTo(), getDtAltFrom(), getDtAltTo(), getStatus(), extraArgumentsDTO, true );		
				
		//agora complementa a lista retornada com os fetchprofiles necessarios para trazer as colecoes de participacoes e payloads
		//List<EventVO> eventosEncontrados = eventBusiness.findBeansByBeans(resultados,"event_participations","participations_payloadVOs");
		
		List<EventVO> eventosEncontrados = eventBusiness.findEvents(resultados, extraArgumentsDTO);
		
		//bingo: lista paginada corretamente, mas com os fetchprofiles desejados
		return eventosEncontrados;
	}

	
	@Override
	public String invalidateRowCountCache() {
		
		getCriterionDataModel().getJazzDataProvider().invalidateRowCountCache();
		getPayloadDataModel().getJazzDataProvider().invalidateRowCountCache();
		
		return super.invalidateRowCountCache();
	}
	
	
	/**
	 * Implementação de contagem de registros específica para este módulo. Este valor será cacheado até que o método invalidateRowCountCache() seja acionado. Na
	 * solução de design proposta o cache de rowCount é válido até que os argumentos de pesquisa sejam modificados.
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl#rowCount()
	 */
	@Override
	protected Long rowCount() {
		EventBusiness eventBusiness = (EventBusiness) getBusiness();
		Long countEventVO = eventBusiness.countEventVO(getDescription(), getDtFimFrom(), getDtFimTo(), getDtInicioFrom(), getDtInicioTo(), getDtIncFrom(), getDtIncTo(), getDtAltFrom(), getDtAltTo(), getStatus(),true);
		return countEventVO;
	}
	

	/**
	 * Cria datamodel paginado para exibir tabela de payloads
	 * @return
	 */
	public JazzDataModel<PayloadVO> getPayloadDataModel() {
		
		if(this.payloadDataModel==null){
			this.payloadDataModel = new JazzDataModel<PayloadVO>(new AbstractDataProvider<PayloadVO>() {
				
				/**
				 * Implementação de pesquisa do datamodel
				 */
				public List<PayloadVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
					
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					
					String[] fetchProfile = new String[]{""};
					
					List<PayloadVO> resultados = eventBusiness.findPayloadVO(extraArgumentsDTO, fetchProfile);
					
					return resultados;
				}

				/**
				 * Testa disponibilidade do registro
				 */
				public boolean isRowAvailable(Serializable currentPk) {
					return currentPk!=null;
				}

				/**
				 * Conta registros totais que deveriam ser retornado pela mesma pesquisa, mas sem paginação
				 */
				public Long cachedRowCount() {
					EventBusiness eventBusiness = (EventBusiness) getBusiness();
					return eventBusiness.countNotProcessedPayloadVO();
				}


				
			});
		}
		
		return this.payloadDataModel;
	}


	/**
	 * 
	 * @return
	 */
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


	public PayloadVO getSelPayloadVO() {
		return selPayloadVO;
	}


	/**
	 * Acionado como action method. 
	 * Complementa payload selecionado as imagens deste. 
	 * Executa a pesquisa por chave do próprio payload mas com um fetch profile para trazer as imagens
	 *  
	 * @param selPayloadVO
	 */
	public void setSelPayloadVO(PayloadVO selPayloadVO) {
		if (selPayloadVO != null) {
			if (selPayloadVO.getPK() != null) {
				EventBusiness business = (EventBusiness) getBusiness();
				selPayloadVO = business.findPayloadByPK(selPayloadVO, "payload_com_imagens");
			}
		}

		this.selPayloadVO = selPayloadVO;
	}


	public ParticipationVO getSelParticipationVO() {
		return selParticipationVO;
	}


	public void setSelParticipationVO(ParticipationVO selParticipationVO) {
		this.selParticipationVO = selParticipationVO;
	}


	public List<CriterionDetailDTO> getQuestionDetails() {
		return null;
	}


	public void mapQuestionsInActualPage() {

	}


	/**
	 * @return
	 */
	protected Integer getSelectedPage() {
		return 1;
	}

	
	public void setQuestionDetails(List<CriterionDetailDTO> questionResults) {
	}

	
	public List<CriterionDetailDTO> getMapCriterionResultsValues(){
		return null;
	}
	


	public Long getScreenImgHeight() {
		return screenImgHeight;
	}


	public void setScreenImgHeight(Long examImageSize) {
		this.screenImgHeight = examImageSize;
	}



	public String getDarcio(){
		return "resDarcio";
	}
	
	
	
	public CriterionDetailDTO getCriterionDetailDTO() {
		
		return criterionDetailDTO; 
	}



	public void setCriterionDetailDTO(CriterionDetailDTO criterionDetailDTO) {
		this.criterionDetailDTO = criterionDetailDTO;
	}

	
	
	

}
