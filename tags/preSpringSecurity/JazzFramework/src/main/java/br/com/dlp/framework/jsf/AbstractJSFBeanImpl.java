/**
 * 
 */
package br.com.dlp.framework.jsf;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ISortable;

/**
 * Implementação abstrata de JSF Bean
 * 
 * @author dpacifico
 */
public abstract class AbstractJSFBeanImpl<B extends IBaseVO<? extends Serializable>> implements Serializable {
	
	private static final int DEFAULT_IMAGE_HEIGHT = 100;
	private static final int DEFAULT_IMAGE_WIDTH = 120;
	public static final String EXIBE_PESQUISA = "exibePesquisa";
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = -5765604081444187595L;
	
	
	/**
	 * Resultados consulta
	 */
	protected List<B> resultados = new ArrayList<B>();
	
	/**
	 * Novo bean (inclusão) ou bean selecionado para edição (alteração).
	 */
	public B tmpVO;
	private Integer idleTimeout = 4 * 60 * 1000; // 4 minutos
	private BeanUtilsBean beanUtils;
	

	/**
	 * Cria saida de imagem padrao do Primefaces para p:graphicImage
	 * @param bi
	 * @param strImageLabel
	 * @return
	 * @throws IOException
	 */
  public StreamedContent drawBufferedImage(BufferedImage bi, String strImageLabel ) throws IOException {
  	return drawBufferedImage(bi, strImageLabel, AbstractJSFBeanImpl.DEFAULT_IMAGE_HEIGHT, AbstractJSFBeanImpl.DEFAULT_IMAGE_WIDTH);
	}	
	

	/**
	 * Cria saida de imagem padrao do Primefaces para p:graphicImage
	 * @param bi
	 * @param strImageLabel
	 * @return
	 * @throws IOException
	 */
  public StreamedContent drawBufferedImage(BufferedImage bi, String strImageLabel, Integer height, Integer width ) throws IOException {
  	
  	BufferedImage destBi = scaleImage(bi, height, width);
  	
  	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(destBi, "gif", baos);
				
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		
		
		DefaultStreamedContent streamedContent = new DefaultStreamedContent(bais,"image/gif",strImageLabel);
	  		
  	
  	return streamedContent;
	}


	/**
	 * @param bi
	 * @param height
	 * @param width
	 * @return
	 */
	protected BufferedImage scaleImage(BufferedImage bi, Integer height, Integer width) {
		Double imgWidth 	= (double) bi.getWidth();
  	Double imgHeight = (double) bi.getHeight();

  	Double widthDest = width.doubleValue();
  	Double heightDest = height.doubleValue();
  	
  	Double WScale = widthDest/imgWidth;
  	Double hScale = heightDest/imgHeight;
  	

  	AffineTransform at = new AffineTransform();
  	at.scale(WScale,hScale);
  	
    BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

    BufferedImage destBi = bio.filter(bi, null);
		return destBi;
	}	
	

  /**
   * Cria saida de imagem padrao do Primefaces para p:graphicImage
   * @param bi
   * @return
   * @throws IOException
   */
  public StreamedContent drawBufferedImage(BufferedImage bi ) throws IOException {
  	return drawBufferedImage(bi,"");
	}	
	

	
	
	/**
	 * Sobe o objeto uma posicao na lista
	 * @param object
	 * @param lista
	 */
	public void upInList(Object object, List lista){
		
		Integer indx = lista.indexOf(object);
		
		if(indx==-1){
			//nao achou o objeto na lista
			return;
		}
		
		Integer indxUp = indx+1;
		
		//o destino está além da lista?
		if(indxUp >= lista.size()){
			return;
		}
		
		
		switchPosition(lista, indx, indxUp);
		
	}
	/**
	 * Desce o objeto uma posicao na lista
	 * @param object
	 * @param lista
	 */
	public void downInList(ISortable sortable, List lista){
		
		Integer indx = lista.indexOf(sortable);
		
		if(indx==-1){
			//nao achou o objeto na lista
			return;
		}
		
		Integer indxDown = indx-1;
		
		//o destino esta fora da lista?
		if(indxDown <0){
			return;
		}
		
		switchPosition(lista, indx, indxDown);
		
	}

	
	/**
	 * TODO TESTAR, CRIAR TESTE UNITARIO
	 * @param lista
	 * @param indxFrom
	 * @param indxTo
	 */
	protected void switchPosition(List lista, Integer indxFrom, Integer indxTo) {
		Object objFrom = lista.get(indxFrom);
		Object objTo = lista.get(indxTo);
		
		lista.set(indxFrom, null);
		lista.set(indxTo, 	null);
		
		lista.set(indxFrom, objTo);
		lista.set(indxTo,	 	objFrom);

		
		if(objFrom != null && objTo!=null && objFrom instanceof ISortable && objTo instanceof ISortable  ){
			ISortable sortFrom 	= (ISortable) objFrom;
			ISortable sortTo 		= (ISortable) objTo;
	
			Double orderFrom		= sortFrom.getOrdem();
			Double orderTo			= sortTo.getOrdem();
			
			if(orderFrom==null){
				orderFrom = new Double(indxFrom);
			}
			
			if(orderTo==null){
				orderTo = new Double(indxTo);
			}
			
			if(orderFrom.equals(orderTo)){
				if(indxTo>indxFrom){
					orderTo = orderTo+0.1;
				}else{
					orderFrom = orderFrom+0.1;
				}
			}
			
			sortFrom	.setOrdem(orderTo);
			sortTo		.setOrdem(orderFrom);
		}
		
	}
	
	
	
	/**
	 * Ao tentar atribuir null a uma java.util.Date o bean utils dá um erro. Este converter simples corrige o problema.
	 * @author t_dpacifico
	 */
	public final class DateDummyConverter implements Converter {
		public Object convert(Class type, Object value) {
			return value;
		}
	}
	
	public Principal getPrincipal(){
		Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
				
		return principal;
	}
	
	/**
	 * @param tmpVO the tmpVO to set
	 */
	public void setTmpVO(B tmpVO) {
		this.tmpVO = tmpVO;
	}
	
	/**
	 * 
	 * @return the tmpVO
	 */
	public B getTmpVO() {
		return tmpVO;
	}
	
	/**
	 * Método de validação. Beta testando
	 * @param context
	 * @param toValidate
	 * @param value
	 */
	public void validatePopUp(FacesContext context, UIComponent toValidate,	Object value) {
		UIInput uiInput = (UIInput) toValidate;
		FacesMessage message = new FacesMessage("Invalid Email");
		context.addMessage(toValidate.getClientId(context), message);
		uiInput.setValid(false);
	}
	
	
	/**
	 * Método Callback referente à retomada de atividade. Este método é acionado
	 * ao fechado a mensagem de diálogo sobre inatividade na tela.
	 * 
	 * @param facesEvent
	 */
	public void activityEvent(CloseEvent facesEvent) {
		addMessage(FacesMessage.SEVERITY_INFO, "Timeout de sessão reiniciado!");
	}
	
	/**
	 * Apenas posiciona o registros selecionado em this.tmpVO
	 * 
	 * @param voBean
	 * @return
	 */
	public String actionDelete(final B voBean) {
		setTmpVO(voBean);
		
		return null;
	}
	
	/**
	 * Confirma deleção de um registro selecionado
	 * 
	 * @return
	 * @throws JazzBusinessException
	 */
	public String deleteConfirm() throws JazzBusinessException {
		if (this.getTmpVO() != null) {
			
			final IBusiness<B> business = this.getBusiness();
			business.delete(this.getTmpVO());
			
			addMessage(FacesMessage.SEVERITY_WARN,
			"Registro excluído com sucesso!");
		} else {
			
			addMessage(FacesMessage.SEVERITY_WARN,
					"Erro ao tentar excluir registro!",
			"Não há nenhum registro selecionado no momento!");
			log.warn("O metodo deleteConfirmation foi acionado, mas nao há nenhum registro selecionado");
		}
		
		this.setTmpVO(null);
		return null;
	}
	
	/**
	 * Cancela operação de deleção de registro
	 * 
	 * @return
	 */
	public String deleteCancel() {
		this.setTmpVO(null);
		addMessage(FacesMessage.SEVERITY_WARN, "OPERAÇÃO CANCELADA!",
		"Nenhum registro foi excluído!");
		
		return null;
	}
	
	/**
	 * Adiciona mensagem de diálogo para o usuário
	 * 
	 * @param severityInfo
	 * @param summary
	 */
	protected void addMessage(Severity severityWarn, String string) {
		this.addMessage(severityWarn, string, "");
	}
	
	/**
	 * Implementação genérica para botão "Novo". Prepara estado da tela para
	 * criação de um novo registro.
	 * 
	 * @return
	 */
	public String actionNovo() {
		final IBusiness<B> business = this.getBusiness();
		setTmpVO(business.newVO());
		
		addMessage(FacesMessage.SEVERITY_INFO,
		"Preencha os dados do novo registro...");
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	/**
	 * Método de pesquisa padrão
	 * 
	 * @return
	 */
	public abstract String actionPesquisar();
	
	
	/**
	 * TODO: IMPLEMENTAR FORMATACAO DE CAMPOS EXIBIDOS EM PESQUISA
	 * TODO: IMPLEMENTAR CONTROLE DE READ ONLY
	 * Limpa os critérios de pesquisa do módulo
	 * 
	 * @return
	 */
	public String actionLimparPesquisa(){
		
		String[] camposLimpar = getCamposLimparPesquisa();
		
		for (String campo : camposLimpar) {
			try {
				
				BeanUtilsBean instance = getBeanUtilsBean();
				instance.setProperty(this, campo, null);
				
			} catch (ConversionException e) {
				log.warn("Erro ao tentar efetuar limpeza do campo '"+campo+"' deste bean!",e);
				throw new JazzRuntimeException("Erro ao tentar efetuar limpeza do campo '"+campo+"' deste bean!",e);
			} catch (IllegalAccessException e) {
				log.warn("Erro ao tentar efetuar limpeza do campo '"+campo+"' deste bean!",e);
				throw new JazzRuntimeException("Erro ao tentar efetuar limpeza do campo '"+campo+"' deste bean!",e);
			} catch (InvocationTargetException e) {
				log.warn("Erro ao tentar efetuar limpeza do campo '"+campo+"' deste bean!",e);
				throw new JazzRuntimeException("Erro ao tentar efetuar limpeza do campo '"+campo+"' deste bean!",e);
			}
		}
		//refresh();
		return actionPesquisar();
	}
	
	
	
	public void refresh() {
		
		
		
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		UIComponent panel = context.getViewRoot().findComponent("j_id7:pnlPesquisa");
		
		resetUIComponent(panel);
		
	}
	
	/**
	 * @param component
	 */
	protected void resetUIComponent(UIComponent component) {
		Iterator<UIComponent> childrens = component.getFacetsAndChildren();
		
		if(component instanceof UIInput){
			UIInput uiInput = (UIInput) component;
			uiInput.setSubmittedValue(null);
		}
		
		while(childrens.hasNext()){
			resetUIComponent(childrens.next());
		}
		
	}
	
	
	
	
	/**
	 * @return
	 */
	protected BeanUtilsBean getBeanUtilsBean() {
		if(this.beanUtils ==null){
			
			BeanUtilsBean instance = BeanUtilsBean.getInstance();
			instance.getConvertUtils().register(new DateDummyConverter(), Date.class);
			this.beanUtils = instance;
		}
		return this.beanUtils;
	}
	
	/**
	 * Retornar o nome dos campos deste bean, ou de suas
	 * especializações, que deverão ser anulados para limpar os critérios de pesquisa.
	 * <br/>
	 * O método actionLimparPesquisa() beanUtilsBean e estes campos para realizar a limpeza por reflection
	 * @return
	 */
	public abstract String[] getCamposLimparPesquisa();
	
	/**
	 * Salva registro que está sendo editado na página
	 * 
	 * @return
	 * @throws JazzBusinessException
	 */
	public String actionSalvar() throws JazzBusinessException {
		if (getTmpVO() != null) {
			final IBusiness<B> business = this.getBusiness();
			
			try {
				business.saveOrUpdate(getTmpVO());
			} catch (ConstraintViolationException e) {
				parseViolations(e);
				return null;
			}
			
			addMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "");
			actionPesquisar();
			
			// deseleciona o registro incluído ou editado e fecha modal
			successSaveState();
						
			return null;
		} else {
			
			return null;
		}
	}
	
	
	/**
	 * Muda estado do jsfbean para que o modal de manutencao feche
	 */
	public void successSaveState(){
		//setTmpVO(null);
	}
	
	/**
	 * Converte as exceções apontadas em validações automáticas em mensagens
	 * padrão JSF
	 * 
	 * @param e
	 */
	protected void parseViolations(ConstraintViolationException e) {
		
		
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		
		FacesContext context = FacesContext.getCurrentInstance();
		for (ConstraintViolation<?> constraintViolation : violations) {
			context.addMessage(getClientID(constraintViolation), createFacesMessage(constraintViolation));
		}
		
	}

	/**
	 * Determina o clientId para o constraintviolation capturado
	 * @param constraintViolation
	 * @return
	 */
	protected String getClientID(ConstraintViolation<?> constraintViolation) {
		return getFrmManutencao()+constraintViolation.getPropertyPath().toString();
	}
	
	/**
	 * Cria uma FacesMessage a partir de uma constraintviolation
	 * @param constraintViolation
	 * @return
	 */
	protected FacesMessage createFacesMessage(ConstraintViolation<?> constraintViolation) {
		return new FacesMessage(constraintViolation.getMessage());
	}
	
	/**
	 * Retorna o nome do formulário com os campos de manutenção
	 * @return
	 */
	protected String getFrmManutencao() {
		return "frmManutencao:";
	}
	
	
	
	/**
	 * Adiciona mensagem de diálogo para o usuário
	 * 
	 * @param severityInfo
	 * @param summary
	 * @param detail
	 */
	protected void addMessage(Severity severityInfo, String summary,
			String detail) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null,
				new FacesMessage(severityInfo, summary, detail));
	}
	
	/**
	 * Preparar para alteracao
	 * 
	 * @param voBean
	 * @return
	 * @throws JazzBusinessException
	 */
	public String actionUpdate(final B voBean) throws JazzBusinessException {
		setTmpVO(voBean);
		addMessage(FacesMessage.SEVERITY_INFO,
		"Edite o registro selecionado e pressione Salvar!");
		return AbstractJSFBeanImpl.EXIBE_PESQUISA;
	}
	
	

	/**
	 * Monta lista de selectItem para qualquer enum informado
	 * @param <E>
	 * @param enumClass
	 * @return
	 */
 	public <E extends Enum<E>> List<SelectItem> getEnums(Class<E> enumClass){
 		
 		List<SelectItem> itens = new ArrayList<SelectItem>();
 		
		EnumSet<E> enumSet = EnumSet.allOf(enumClass); 
 		
 		for (E enumItem : enumSet) {
			
 			SelectItem selectItem = new SelectItem();
 			
 			selectItem.setValue(enumItem);
 			selectItem.setLabel(enumItem+"");
 			
 			itens.add(selectItem);
 			
		}
 		
 		return itens;
 	}
	
	
	/**
	 * Retorna o BusinessObject da implementação concreta do módulo
	 * 
	 * @return IBusiness implementation
	 */
	protected abstract IBusiness<B> getBusiness();
	
	public List<B> getResultados() {
		return resultados;
	}
	
	public void setResultados(List<B> resultados) {
		this.resultados = resultados;
	}
	
	
	public Integer getIdleTimeout() {
		return idleTimeout;
	}
	
	public void setIdleTimeout(Integer idleTimeout) {
		this.idleTimeout = idleTimeout;
	}
	
}
