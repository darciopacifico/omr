/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRJSFBeanImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.EmpresaBusiness;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.person.EAuthority;

/**
 * Incluir arquivo com comentários
 * 
 * Implementação de Bean JSF para o componente ExamVO
 * 
 **/
@Scope(value = "session")
@Component
public class ExamJSFBean extends AbstractJazzOMRJSFBeanImpl<ExamVO> {

	private static final long serialVersionUID = 2195241915100521959L;

	@Autowired
	private ExamBusiness examBusiness;
	
	@Autowired
	private EmpresaBusiness empresaBusiness;

	@Autowired
	private QuestionnaireBusiness questionnaireBusiness;

	
	private DualListModel<QuestionnaireVO> questionnaireDualListModel = null;	
	
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */

	private String description;
	private Date dtIncFrom;
	private Date dtIncTo;
	private Date dtAltFrom;
	private Date dtAltTo;
	private StatusEnum status;

	private List<QuestionnaireVO> freeQuestionnaires = new ArrayList<QuestionnaireVO>();

	
	/**
	 * Recupera os relatorios de exame registrados para esta empresa.
	 * @return
	 */
	public List<SelectItem> getEnterpriseReports(){
		
		List<RelatorioVO> relatorios = empresaBusiness.findRelatorios();
		
		List<SelectItem> selectItems = new ArrayList<SelectItem>(relatorios.size());
		
		for (RelatorioVO relatorioVO : relatorios) {
			
			SelectItem selectItem = new  SelectItem();
			selectItem.setLabel(relatorioVO.getDescription());
			selectItem.setDescription(relatorioVO.getDescription());
			selectItem.setValue(relatorioVO);
			
			selectItems.add(selectItem);
		}
		
		return selectItems;
		
	}

	

	public void setQuest (DualListModel<QuestionnaireVO> quest){
		this.questionnaireDualListModel = quest;
		
		if(this.questionnaireDualListModel!=null){
			getTmpVO().setQuestionnaires(this.questionnaireDualListModel.getTarget());
		}
	}
	
	public DualListModel<QuestionnaireVO> getQuest(){
		
		
		if(questionnaireDualListModel==null){
			ExamVO tmpVO2 = getTmpVO();
			if(tmpVO2==null){
				return new DualListModel<QuestionnaireVO>();
			}
			
			List<QuestionnaireVO> questionnairesNotIn = questionnaireBusiness.findAllNotIn(tmpVO2.getQuestionnaires());
			List<QuestionnaireVO> quest = new ArrayList<QuestionnaireVO>(questionnairesNotIn.size());
			quest.addAll(questionnairesNotIn);
			questionnaireDualListModel = new DualListModel<QuestionnaireVO>(quest, tmpVO2.getQuestionnaires());
		}
	
		return questionnaireDualListModel;
		
	}
	
	
	@Override
	public ExamVO getTmpVO() {
		
		ExamVO tmpVO2 = super.getTmpVO();
		
		
		return tmpVO2;
	}
	
	
	public List<? extends Enum> autorizeAnyOf(){
		
		ArrayList<EAuthority> authorities = new ArrayList<EAuthority>();
		
		authorities.add(EAuthority.MASTER_ADM);
		authorities.add(EAuthority.PROFESSOR);
		authorities.add(EAuthority.ASSISTENTE_PROF);
		
		return authorities;
		
	}
	
	
 	@Override
 	public void setTmpVO(ExamVO tmpVO) {
 		
 		if(tmpVO!=null && tmpVO.getPK()!=null){
 			IJazzOMRBusiness<ExamVO> business = (IJazzOMRBusiness) getBusiness();
 			tmpVO = business.findByPK(tmpVO.getPK());
 			
 		}
 		
 		super.setTmpVO(tmpVO);
 	}


 	@Override
 	public String actionNovo() {
 		String actionNovo = super.actionNovo();
 		
 		setQuest(null);
 		
		return actionNovo;
 	}
 	

	
	/**
	 * Implementacao do método de pesquisa de IJazzDataProvider específico para este módulo. Pesquisa e retorna lista de ExamVO com paginação e ordenação de dados.
	 * 
	 * @see br.com.dlp.framework.jsf.IJazzDataProvider#actionPesquisar(br.com.dlp.framework.dao.ExtraArgumentsDTO)
	 */
	public List<ExamVO> actionPesquisar(ExtraArgumentsDTO extraArgumentsDTO) {
		List<ExamVO> resultados = examBusiness.findExamVO(description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
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
		return examBusiness.countExamVO(description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
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
	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar...
	 * @return
	 */
	public List<SelectItem> getStatusList() {
		return getEnums(StatusEnum.class);
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
		return new String[] { "description", "dtIncFrom", "dtIncTo", "dtAltFrom", "dtAltTo", "status" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.jsf.AbstractJSFBeanImpl#getBusiness()
	 */
	@Override
	protected IBusiness<ExamVO> getBusiness() {
		return this.examBusiness;
	}

	


}