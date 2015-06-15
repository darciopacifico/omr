/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.AbstractJazzOMRJSFBeanImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.EmpresaVO;
import br.com.dlp.jazzomr.person.EAuthority;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeScore;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.DissertativeVO;
import br.com.dlp.jazzomr.question.QuestionType;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.question.Verbosity;
import br.com.dlp.jazzomr.results.ImageVO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de Bean JSF para o componente QuestionnaireVO
 *
 **/
@Scope(value="session")
@Component
public class QuestionnaireJSFBean extends AbstractJazzOMRJSFBeanImpl<QuestionnaireVO> {
	
	public static final int MAX_IMAGES_PER_QUESTION = 3;

	private static final long serialVersionUID = 2195241915100521959L;
		
	@Autowired
	private QuestionnaireBusiness questionnaireBusiness;

	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	
	private String selectedTab;

	private ISortable deleteQuestion;
	private Set<QuestionVO> questions;
 	private String description;
 	private Date dtIncFrom;
 	private Date dtIncTo;
 	private Date dtAltFrom;
 	private Date dtAltTo;
 	private StatusEnum status;

	private QuestionVO editQuestion;

	
	
 		
	public String getTitulo0() {
		return getTitulo(0);
	}


	private String getTitulo(int index) {
		return getEditQuestion().getImageVOs().get(index).getTitulo();
	}

	private void setTitulo(int index, String titulo) {
		getEditQuestion().getImageVOs().get(index).setTitulo(titulo);
	}


	public void setTitulo0(String titulo0) {
		setTitulo(0, titulo0);
	}


	public String getTitulo1() {
		return getTitulo(1);
	}


	public void setTitulo1(String titulo1) {
		setTitulo(1, titulo1);
	}


	public String getTitulo2() {
		return getTitulo(2);
	}


	public void setTitulo2(String titulo2) {
		setTitulo(2, titulo2);
	}


	public List<? extends Enum> autorizeAnyOf(){
		
		ArrayList<EAuthority> authorities = new ArrayList<EAuthority>();
		
		authorities.add(EAuthority.MASTER_ADM);
		authorities.add(EAuthority.PROFESSOR);
		authorities.add(EAuthority.ASSISTENTE_PROF);
		
		return authorities;
		
	}
	
	
	/**
	 * Action
	 * @param event
	 * @throws IOException 
	 */
  public void actionHandleFileUpload(UploadEvent event) throws IOException {  
  	QuestionVO editQuestion = getEditQuestion();
  	
  	if(editQuestion==null){
  		return;
  	}
  	
  	if(editQuestion.getImageVOs().size()>=QuestionnaireJSFBean.MAX_IMAGES_PER_QUESTION){
  		addMessage(FacesMessage.SEVERITY_ERROR, "Não é possível carregar mais de "+QuestionnaireJSFBean.MAX_IMAGES_PER_QUESTION+" imagens por questão!", "");
  		return;
  	}
  	

  	List<UploadItem> files = event.getUploadItems();
  	
  	for (UploadItem uploadItem : files) {
  		if(isImage(uploadItem)){
  			attachQuestionImage(editQuestion, uploadItem);
  		}
		}  	
  	
    addMessage(FacesMessage.SEVERITY_INFO, "Imagens carregadas com sucesso!", "");
  }



	/**
	 * @param editQuestion
	 * @param uploadItem
	 * @throws IOException
	 */
	protected void attachQuestionImage(QuestionVO editQuestion, UploadItem uploadItem) throws IOException {
		byte[] imgBytes = uploadItem.getData();
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
		
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		
		List<ImageVO> images = editQuestion.getImageVOs();
		
		ImageVO imageVO = new ImageVO();
		
		imageVO.setImage(bufferedImage);
		
		images.add(imageVO);
		
	}



	/**
	 * @param uploadItem
	 * @return
	 */
	protected boolean isImage(UploadItem uploadItem) {
		String contentType=uploadItem.getContentType();
		
		String imageTypes = 
		
		"#"+"image/gif"+"#"+
		"#"+"image/jpeg"+"#"+
		"#"+"image/png"+"#"+
		"#"+"image/x-png"+"#"+
		"#"+"image/pjpeg"+"#"+
		"#"+"image/tiff"+"#"+
		"#"+"image/bmp"+"#";
		
		return imageTypes.contains(contentType);
		
	}

 	
 	
 	@Override
 	public void setTmpVO(QuestionnaireVO tmpVO) {
 		
 		if(tmpVO!=null && tmpVO.getPK()!=null){
 			IJazzOMRBusiness<QuestionnaireVO> business = (QuestionnaireBusiness) getBusiness();
 			tmpVO = business.findByPK(tmpVO, "questinario_com_questoes","questao_com_alternativas");
 			//tmpVO = business.findByBeanPK(tmpVO);
 		}
 		
 		super.setTmpVO(tmpVO);
 	}

 	
 	/**
 	 * Valida a questão que esta sendo editada antes de tentar salvar o questionário todo.
 	 * @return
 	 * @throws JazzBusinessException
 	 */
	public String actionSalvarQuestao() throws JazzBusinessException {
		
		
		
		Set<ConstraintViolation<QuestionVO>> constraintViolations = consistEditQuestion();
		
		if(CollectionUtils.isNotEmpty(constraintViolations)){
			//possui a alguma inconsistencia
			return null;
		}
		
		
		String actionSalvar = super.actionSalvar();
		
		return actionSalvar;
	}



	/**
	 * @return
	 */
	protected Set<ConstraintViolation<QuestionVO>> consistEditQuestion() {
		QuestionVO editQuestion2 = getEditQuestion();

		if(editQuestion2==null){
			return new HashSet<ConstraintViolation<QuestionVO>>(0);
		}
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<QuestionVO>> constraintViolations = validator.validate(editQuestion2);
		
		parseViolations(constraintViolations);
		return constraintViolations;
	}
	

 	
 	/**
 	 * Valida a questão que esta sendo editada antes de tentar salvar o questionário todo.
 	 * @return
 	 * @throws JazzBusinessException
 	 */
	public String actionSalvarQuestaoNova() throws JazzBusinessException {
		
		Set<ConstraintViolation<QuestionVO>> constraintViolations = consistEditQuestion();
		
		if(CollectionUtils.isNotEmpty(constraintViolations)){
			//possui a alguma inconsistencia
			return null;
		}
		
		invalidateRowCountCache();
		
		if (getTmpVO() != null) {

			final QuestionnaireBusiness business = (QuestionnaireBusiness) this.getBusiness();
			
			try {
				business.saveOrUpdate(getTmpVO());
			} catch (JazzBusinessException e) {
				parseBusinessException(e);
				return null;
			} catch (ConstraintViolationException e) {
				parseViolations(e);
				return null;
			}
			
			addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "");
			actionPesquisar();
			
			// deseleciona o registro incluído ou editado e fecha modal
			successSaveState();
						

		
		}
		
		
 		
		QuestionnaireVO questionnaireVO = getTmpVO();
		
		if(StringUtils.isBlank(questionnaireVO.getDescription())){
			addMessage(FacesMessage.SEVERITY_INFO, "Por favor, dê uma descrição ao questionário antes de incluir uma questão!", "");
			return "";
		}
		
 		QuestionVO questionVO = new QuestionVO();
 		questionVO.setEmpresaVO(getEmpresaUsuarioLogado());
 		List<QuestionVO> questionList = questionnaireVO.getQuestions();
 		
		questionList.add(questionVO);
		Double idx = new Double(questionList.indexOf(questionVO));
		questionVO.setOrdem(idx);
 		
 		setEditQuestion(questionVO);

		
		return null;
	}
	
	
	
	public String removeFromEditQuestion(AbstractCriterionVO criterionVO){
		
		QuestionVO questionVO = getEditQuestion();
		
		if(questionVO==null){
			log.warn("Comportamento anormal. Esta se tentando retirar o criterio "+criterionVO+" sem que haja uma questao selecionada!");
			return "";
		}
		
		//TODO: VERIFICAR PORQUE DIABOS UM SET NORMAL NAO REMOVE UM REGISTRO LOGO APOS SAVE DO HIBERNATE.
		//
		List<AbstractCriterionVO> lista = new ArrayList<AbstractCriterionVO>(questionVO.getCriterionVOs());
		lista.remove(criterionVO);
		questionVO.setCriterionVOs(new HashSet<AbstractCriterionVO>(lista));
			
		return null;
	}

	/**
	 * @param context
	 * @param violations
	 */
	protected void parseViolations(Set<ConstraintViolation<QuestionVO>> violations) {
		FacesContext context = FacesContext.getCurrentInstance();
		for (ConstraintViolation<?> constraintViolation : violations) {
			FacesMessage createFacesMessage = createFacesMessage(constraintViolation);
			String clientID = getClientID(constraintViolation);
			context.addMessage(clientID, createFacesMessage);
		}
	}
	
	
	public String sairDetalheQuestao(){
		//volta o questionário selecionado ao último estado do banco de dados 
		QuestionnaireVO questionnaireVO = getTmpVO();
		setTmpVO(questionnaireVO);
		
		return null;
	}
	
	
 	public void newQuestion(){
 		
		QuestionnaireVO questionnaireVO = getTmpVO();
		
		if(StringUtils.isBlank(questionnaireVO.getDescription())){
			addMessage(FacesMessage.SEVERITY_INFO, "Por favor, dê uma descrição ao questionário antes de incluir uma questão!", "");
			return;
		}
		
 		QuestionVO questionVO = new QuestionVO();

 		EmpresaVO empresaVO = getEmpresaUsuarioLogado();
 		questionVO.setEmpresaVO(empresaVO);
 		
 		List<QuestionVO> questionList = questionnaireVO.getQuestions();
 		
		questionList.add(questionVO);
		Double idx = new Double(questionList.indexOf(questionVO));
		questionVO.setOrdem(idx);
 		
 		setEditQuestion(questionVO);
 	}
 	
 	public void removeQuestion(){
 		QuestionnaireVO questionnaireVO = getTmpVO();
 		questionnaireVO.getQuestions().remove(deleteQuestion);
 		this.deleteQuestion=null;
 	}
 	
 	public void newAlternative(){
 		if(editQuestion==null){
 			return;
 		}

 		int newAlternatives=1;
 		
 		if( CollectionUtils.isEmpty(editQuestion.getAlternativeVOs())){
 			newAlternatives=5;
 		}
 		
 		for(int i=0; i<newAlternatives; i++){
 			
 			AlternativeVO alternativeVO = new AlternativeVO();
 			Set<AbstractCriterionVO> criterionVOs = editQuestion.getCriterionVOs();
 			criterionVOs.add(alternativeVO);
 			alternativeVO.setOrdem(new Double(criterionVOs.size()));

 		}
 		
 	} 	
 	
 	public void newDissertative(){
 		DissertativeVO dissertativeVO = new DissertativeVO();
 		if(editQuestion==null){
 			return;
 		}
 		
 		Set<AbstractCriterionVO> criterionVOs = editQuestion.getCriterionVOs();
 		
		criterionVOs.add(dissertativeVO);
		
		dissertativeVO.setOrdem(new Double(criterionVOs.size()));
 	}
 	
 	/**
 	 * TODO: CONSERTAR O LIMPAR PARA COMBOBOX
 	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar... 
 	 * @return
 	 */
	public List<SelectItem> getStatusList(){
		return getEnums(StatusEnum.class);
	} 	
	
 	/**
 	 * TODO: CONSERTAR O LIMPAR PARA COMBOBOX
 	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar... 
 	 * @return
 	 */
	public List<SelectItem> getScoreList(){
		return getEnums(AlternativeScore.class);
	} 	
	
	
	

 	/**
 	 * TODO: CONSERTAR O LIMPAR PARA COMBOBOX
 	 * Recupera lista do enum verbosity para composicao de combobox, ou outro componente similar... 
 	 * @return
 	 */
	public List<SelectItem> getVerbosityList(){
		return getEnums(Verbosity.class);
	} 	
	

 	/**
 	 * TODO: CONSERTAR O LIMPAR PARA COMBOBOX
 	 * Recupera lista do enum verbosity para composicao de combobox, ou outro componente similar... 
 	 * @return
 	 */
	public List<SelectItem> getQuestionTypeList(){
		return getEnums(QuestionType.class);
	} 	
	
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo.
	 * Pesquisa e retorna lista de QuestionnaireVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<QuestionnaireVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		
		List<QuestionnaireVO> resultados = questionnaireBusiness.findQuestionnaireVO(questions, description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);		
	
		return resultados;
	}
		
	/**
	 * Implementação de contagem de registros específica para este módulo.
	 * Este valor será cacheado até que o método invalidateRowCountCache() seja acionado.
	 * Na solução de design proposta o cache de rowCount é válido até que os argumentos de pesquisa sejam modificados.
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractPaginatedJSFBeanImpl#rowCount()
	 */
	@Override
	protected Long rowCount() {
		return questionnaireBusiness.countQuestionnaireVO(questions, description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}
	

	/**
	 * Accessor Method
 	 * @return questions
 	 */
	public Set<QuestionVO> getQuestions(){
		Set<QuestionVO> quest = new HashSet<QuestionVO>();
		quest.addAll(this.questions);
		return quest;
	}
	
 	/**
	 * Accessor Method
 	 * @return description
 	 */
	public String getDescription(){
		return this.description;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtIncFrom
 	 */
	public Date getDtIncFrom(){
		return this.dtIncFrom;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtIncTo
 	 */
	public Date getDtIncTo(){
		return this.dtIncTo;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtAltFrom
 	 */
	public Date getDtAltFrom(){
		return this.dtAltFrom;
	}
	
 	/**
	 * Accessor Method
 	 * @return dtAltTo
 	 */
	public Date getDtAltTo(){
		return this.dtAltTo;
	}
	
 	/**
	 * Accessor Method
 	 * @return status
 	 */
	public StatusEnum getStatus(){
		return this.status;
	}
	
	

	/**
	 * Mutator Method
 	 * @param questions
 	 */
	public void setQuestions(Set<QuestionVO> questions){
		this.questions = questions;
	}
	
 	/**
	 * Mutator Method
 	 * @param description
 	 */
	public void setDescription(String description){
		this.description = description;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtIncFrom
 	 */
	public void setDtIncFrom(Date dtIncFrom){
		this.dtIncFrom = dtIncFrom;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtIncTo
 	 */
	public void setDtIncTo(Date dtIncTo){
		this.dtIncTo = dtIncTo;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtAltFrom
 	 */
	public void setDtAltFrom(Date dtAltFrom){
		this.dtAltFrom = dtAltFrom;
	}
	
 	/**
	 * Mutator Method
 	 * @param dtAltTo
 	 */
	public void setDtAltTo(Date dtAltTo){
		this.dtAltTo = dtAltTo;
	}
	
 	/**
	 * Mutator Method
 	 * @param status
 	 */
	public void setStatus(StatusEnum status){
		this.status = status;
	}
	



	/**
	 * Retorna os atributos deste bean utilizados para pesquisa
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getCamposLimparPesquisa()
	 */
	@Override
	public String[] getCamposLimparPesquisa() {
			return new String[]{"questions", "description", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status"};
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<QuestionnaireVO> getBusiness() {
		return this.questionnaireBusiness;
	}


	/**
	 * @return the deleteQuestion
	 */
	public ISortable getDeleteQuestion() {
		return deleteQuestion;
	}


	/**
	 * @param deleteQuestion the deleteQuestion to set
	 */
	public void setDeleteQuestion(ISortable deleteQuestion) {
		this.deleteQuestion = deleteQuestion;
	}

	
	/**
	 * Complementa dados do questionario para posterior abertura da tela de edição 
	 * @param editQuestion
	 * @return
	 */
	public String detailQuestion(QuestionVO editQuestion){
		
		int index = getTmpVO().getQuestions().indexOf(editQuestion);
		
		if(index==-1){
			throw new JazzRuntimeException("Erro ao tentar editar questao!");
		}

		//Se a questão já foi salva, recupera a última versão do banco de dados.
		editQuestion = refreshQuestion(editQuestion);

		getTmpVO().getQuestions().set(index, editQuestion);
		
		setEditQuestion(editQuestion);
		
		return null;
		
	}


	/**
	 * @param editQuestion
	 * @return
	 */
	protected QuestionVO refreshQuestion(QuestionVO editQuestion) {
		QuestionVO newEditQuestion = (QuestionVO) questionnaireBusiness.complementBean(editQuestion, "questao_com_imagens");
		
		if(newEditQuestion!=null){
			editQuestion = newEditQuestion;
		}
		return editQuestion;
	}
	
	/**
	 * @param deleteQuestion the deleteQuestion to set
	 */
	public void setEditQuestion(QuestionVO editQuestion) {
		this.editQuestion = editQuestion;
	}


	/**
	 * @return the selectedTab
	 */
	public String getSelectedTab() {
		return selectedTab;
	}


	/**
	 * @param selectedTab the selectedTab to set
	 */
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}



	public QuestionVO getEditQuestion() {
		return editQuestion;
	}
}