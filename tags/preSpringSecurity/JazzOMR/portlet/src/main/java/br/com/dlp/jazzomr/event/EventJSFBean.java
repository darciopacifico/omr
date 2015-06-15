/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.event;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl;
import br.com.dlp.jazzomr.base.AbstractJazzOMRJSFBeanImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.IJazzOMRBusiness;
import br.com.dlp.jazzomr.person.PessoaVO;

/**
 * Incluir arquivo com comentários
 * 
 * Implementação de Bean JSF para o componente EventVO
 * 
 **/
@Scope(value = "session")
@Component
public class EventJSFBean extends AbstractJazzOMRJSFBeanImpl<EventVO> {
	protected static final DateFormat FILENAME_DTFORMAT = new SimpleDateFormat("ddMMyy_HHmm");
	
	private static final long serialVersionUID = 2195241915100521959L;

	@Autowired
	private EventBusiness eventBusiness;

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	private String description;
	private Date dtFimFrom;
	private Date dtFimTo;
	private Date dtInicioFrom;
	private Date dtInicioTo;
	private Date dtIncFrom;
	private Date dtIncTo;
	private Date dtAltFrom;
	private Date dtAltTo;
	private StatusEnum status;

	@Autowired
	private ParticipationHelper participationHelper;


	/**
	 * Exclui participacao, mas nao salva registro de evento
	 * @param participationVO
	 * @return
	 */
	public String actionDeleteParticipation(ParticipationVO participationVO){
		
		boolean removed = this.getTmpVO().getParticipations().remove(participationVO);
		
		PessoaVO pessoaVO = participationVO.getPessoaVO();
		ExamVariantVO examVariantVO = participationVO.getExamVariantVO();
		ExamVO examVO = examVariantVO.getExamVO();
		String msgPattern;
		
		if(removed){
			msgPattern = "Participação da pessoa \"{0}\", exame \"{1}\" no evento \"{2}\" foi excluída!";
		}else{
			msgPattern = "Erro ao tentar exluir participação da pessoa \"{0}\", exame \"{1}\" no evento \"{2}\"!";
		}
		
		String message = MessageFormat.format(msgPattern, pessoaVO.getNome(),examVO.getDescription(),this.getTmpVO().getDescription());
		
		addMessage(FacesMessage.SEVERITY_INFO, message, "");
		
		return "";
	}
	
	
	/**
	 * Salva dados básicos do evento e adiciona
	 * @param eventVO
	 * @return
	 * @throws JazzBusinessException
	 */
	public String actionAddParticipations(EventVO eventVO) throws JazzBusinessException{
		
		actionSalvar();
		setTmpVO(eventVO);
		
		ParticipationHelper participationHelper = getParticipationHelper();
		participationHelper.resetHelper();
				
		return null;
	}

	/**
	 * Escreve arquivo de exames, referente ao evento escolhido, em HttpServletResponse
	 * @param eventVO
	 */
	public void downloadExamFile(EventVO eventVO) {

		if( CollectionUtils.isEmpty(eventVO.getParticipations())){
			addMessage(FacesMessage.SEVERITY_INFO, "Não há participações registradas para este evento por enquanto.", "");
			return;
		}
		
		byte[] bytesPDF = participationHelper.createPDFBytes(eventVO);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytesPDF);
		
		HttpServletResponse response = prepareHttpResponse(eventVO, bytesPDF, "ExamsEvt_{0}_{1}.pdf");

		writePDFResponse(bais, response);

	}



	
	
	
	

	/**
	 * Prepara httpResponse para a saida de bytes do arquivo pdf
	 * @param eventVO
	 * @param bytesPDF
	 * @param fileNamePattern TODO
	 * @return
	 */
	protected HttpServletResponse prepareHttpResponse(EventVO eventVO, byte[] bytesPDF, String fileNamePattern) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String fileName = getPDFFileName(eventVO, fileNamePattern);
		
		HttpServletResponse response1 = (HttpServletResponse) context.getResponse();
		response1.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response1.setContentLength((int) bytesPDF.length );
		response1.setContentType("application/pdf");
		
		HttpServletResponse response = response1;
		return response;
	}


	/**
	 * Itera array de bytes do pdf escrevendo na saida http
	 * @param bais
	 * @param response
	 */
	protected void writePDFResponse(ByteArrayInputStream bais,
			HttpServletResponse response) {
		try {
			OutputStream out = response.getOutputStream();

			byte[] buf = new byte[1024];
			int count;
			while ((count = bais.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			bais.close();
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();

		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar enviar stream de arquivo pdf para download!", e);
		}
	}

	/**
	 * Formata nome do arquivo
	 * @param eventVO
	 * @param fileNamePattern TODO
	 * @return
	 */
	protected String getPDFFileName(EventVO eventVO, String fileNamePattern) {
		//TODO internacionalizar nomes
		
		String strData = FILENAME_DTFORMAT.format(eventVO.getDtAlt());
		
		String pattern = fileNamePattern;
		
		String fileName = MessageFormat.format(pattern, eventVO.getPK(),strData);
		
		return fileName;
	}

	/**
	 * 
	 * @return the eventVOSaved
	 */
	public Boolean getEventVOSaved() {
		Boolean isEventVOSaved = this.participationHelper.getIsEventVOSaved(this.getTmpVO());
		return isEventVOSaved;
	}	
	
	/**
	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar...
	 * 
	 * @return
	 */
	public List<SelectItem> getStatusList() {
		return getEnums(StatusEnum.class);
	}

	
	/**
	 * Sobrescrita do metodo padrao setTmpVO que carrega relacionamentos da entidade escolhida para edicao
	 */
	@Override
	public void setTmpVO(EventVO tmpVO) {

		
		if(tmpVO!=null && tmpVO.getPK()!=null){
			
			EventBusiness eventBusiness = (EventBusiness) getBusiness();
			
			tmpVO = eventBusiness.findByPK(tmpVO, "event_participations");
			
		}
		
		
		super.setTmpVO(tmpVO);
	}

	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo. Pesquisa e retorna lista de EventVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<EventVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		List<EventVO> resultados = eventBusiness.findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
		return resultados;
	}

	
	/**
	 * Implementação de contagem de registros específica para este módulo. Este valor será cacheado até que o método invalidateRowCountCache() seja acionado. Na
	 * solução de design proposta o cache de rowCount é válido até que os argumentos de pesquisa sejam modificados.
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl#rowCount()
	 */
	@Override
	protected Long rowCount() {
		return eventBusiness.countEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Accessor Method
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtFimFrom
	 */
	public Date getDtFimFrom() {
		return this.dtFimFrom;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtFimTo
	 */
	public Date getDtFimTo() {
		return this.dtFimTo;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtInicioFrom
	 */
	public Date getDtInicioFrom() {
		return this.dtInicioFrom;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtInicioTo
	 */
	public Date getDtInicioTo() {
		return this.dtInicioTo;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtIncFrom
	 */
	public Date getDtIncFrom() {
		return this.dtIncFrom;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtIncTo
	 */
	public Date getDtIncTo() {
		return this.dtIncTo;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtAltFrom
	 */
	public Date getDtAltFrom() {
		return this.dtAltFrom;
	}

	/**
	 * Accessor Method
	 * 
	 * @return dtAltTo
	 */
	public Date getDtAltTo() {
		return this.dtAltTo;
	}

	/**
	 * Accessor Method
	 * 
	 * @return status
	 */
	public StatusEnum getStatus() {
		return this.status;
	}

	/**
	 * Mutator Method
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtFimFrom
	 */
	public void setDtFimFrom(Date dtFimFrom) {
		this.dtFimFrom = dtFimFrom;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtFimTo
	 */
	public void setDtFimTo(Date dtFimTo) {
		this.dtFimTo = dtFimTo;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtInicioFrom
	 */
	public void setDtInicioFrom(Date dtInicioFrom) {
		this.dtInicioFrom = dtInicioFrom;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtInicioTo
	 */
	public void setDtInicioTo(Date dtInicioTo) {
		this.dtInicioTo = dtInicioTo;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtIncFrom
	 */
	public void setDtIncFrom(Date dtIncFrom) {
		this.dtIncFrom = dtIncFrom;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtIncTo
	 */
	public void setDtIncTo(Date dtIncTo) {
		this.dtIncTo = dtIncTo;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtAltFrom
	 */
	public void setDtAltFrom(Date dtAltFrom) {
		this.dtAltFrom = dtAltFrom;
	}

	/**
	 * Mutator Method
	 * 
	 * @param dtAltTo
	 */
	public void setDtAltTo(Date dtAltTo) {
		this.dtAltTo = dtAltTo;
	}

	/**
	 * Mutator Method
	 * 
	 * @param status
	 */
	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
		return new String[] { "description", "dtFimFrom", "dtFimTo", "dtInicioFrom", "dtInicioTo", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<EventVO> getBusiness() {
		return this.eventBusiness;
	}

	/**
	 * @return the participationHelper
	 */
	public ParticipationHelper getParticipationHelper() {
		return participationHelper;
	}

	/**
	 * @param participationHelper
	 *          the participationHelper to set
	 */
	public void setParticipationHelper(ParticipationHelper participationHelper) {
		this.participationHelper = participationHelper;
	}


	/**
	 * @param eventVOSaved the eventVOSaved to set
	 */
	public void setEventVOSaved(Boolean eventVOSaved) {
	}

}