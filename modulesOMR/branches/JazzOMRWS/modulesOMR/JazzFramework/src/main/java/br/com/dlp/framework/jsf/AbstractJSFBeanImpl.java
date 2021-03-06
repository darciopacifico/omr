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
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
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
import javax.validation.groups.Default;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.SetUtils;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ISortable;

/**
 * Implementa��o abstrata de JSF Bean
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
	 * Checa se o usuario logado atualmente pode acessar o modulo
	 */
	public Boolean isAutorized(){
		Enum[] arrayEnums = autorizeAnyOf().toArray(new Enum[0]);
		return ifAnyGranted(arrayEnums);
		//return true;
	}
	
	
	
	public abstract List<? extends Enum> autorizeAnyOf();	
	/**
	 * Resultados consulta
	 */
	protected List<B> resultados = new ArrayList<B>();
	
	/**
	 * Novo bean (inclus�o) ou bean selecionado para edi��o (altera��o).
	 */
	public B tmpVO;
	private Integer idleTimeout = 4 * 60 * 1000; // 4 minutos
	private BeanUtilsBean beanUtils;
	
	/**
	 * Apenas converte qualquer cole��o em lista. Útil para iterar Set TODO: estudar se esta � a melhor saída
	 * @param collection
	 * @return
	 */
	public List toList(Collection collection){
		
		if(collection==null){
			return null;
		}
		
		return new ArrayList(collection);		
	}


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

  public boolean ifAnyGrantedX(String role){
	  return ifAnyGranted(role);
  }
  
  public boolean ifAnyGranted(String... commaRoles){
  	List<String> listRoles = new ArrayList<String>(commaRoles.length);
  	
  	for (String commaRole : commaRoles) {
  		String[] arrayRoles = commaRole.split(",");
  		for (String role : arrayRoles) {
  			listRoles.add(role);
			}
		}
  	
  	return getSecurity().ifAnyGranted(listRoles);
  }

  
	/**
	 * Sobrecarga para trabalhar direto com o enum de authorities
	 * @param eAuths
	 * @return
	 */
	protected boolean ifAnyGranted(Enum... eAuths) {
		
		List<String> strAuths = new ArrayList<String>(eAuths.length);
		for (Enum eAuthority : eAuths) {
			strAuths.add(eAuthority.toString());
		}
		
		String[] arrayAuth = strAuths.toArray(new String[]{});
		
		return ifAnyGranted(arrayAuth);
	}


  
	/**
	 * @return
	 */
	public SecurityHelper getSecurity() {
		return SecurityHelper.getInstance();
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
		
		//o destino est� al�m da lista?
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
	 * Ao tentar atribuir null a uma java.util.Date o bean utils d� um erro. Este converter simples corrige o problema.
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
	 * M�todo de valida��o. Beta testando
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
	 * M�todo Callback referente à retomada de atividade. Este m�todo � acionado
	 * ao fechado a mensagem de di�logo sobre inatividade na tela.
	 * 
	 * @param facesEvent
	 */
	public void activityEvent(CloseEvent facesEvent) {
		addMessage(FacesMessage.SEVERITY_INFO, "Timeout de sess�o reiniciado!");
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
	 * Confirma dele��o de um registro selecionado
	 * 
	 * @return
	 * @throws JazzBusinessException
	 */
	public String deleteConfirm()  {
		if (this.getTmpVO() != null) {
			
			final IBusiness<B> business = this.getBusiness();
			try {
				business.delete(this.getTmpVO());
			} catch (JazzBusinessException e) {
				parseBusinessException(e);
				return null;
			}
			
			addMessage(FacesMessage.SEVERITY_WARN,
			"Registro excluído com sucesso!");
		} else {
			
			addMessage(FacesMessage.SEVERITY_WARN,
					"Erro ao tentar excluir registro!",
			"N�o h� nenhum registro selecionado no momento!");
			log.warn("O metodo deleteConfirmation foi acionado, mas nao h� nenhum registro selecionado");
		}
		
		this.setTmpVO(null);
		return null;
	}
	
	/**
	 * Cancela opera��o de dele��o de registro
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
	 * Adiciona mensagem de di�logo para o usu�rio
	 * 
	 * @param severityInfo
	 * @param summary
	 */
	protected void addMessage(Severity severityWarn, String string) {
		this.addMessage(severityWarn, string, "");
	}
	
	/**
	 * Implementa��o gen�rica para bot�o "Novo". Prepara estado da tela para
	 * cria��o de um novo registro.
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
	 * M�todo de pesquisa padr�o
	 * 
	 * @return
	 */
	public abstract String actionPesquisar();
	
	
	/**
	 * TODO: IMPLEMENTAR FORMATACAO DE CAMPOS EXIBIDOS EM PESQUISA
	 * TODO: IMPLEMENTAR CONTROLE DE READ ONLY
	 * Limpa os crit�rios de pesquisa do m�dulo
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
	 * especializa��es, que dever�o ser anulados para limpar os crit�rios de pesquisa.
	 * <br/>
	 * O m�todo actionLimparPesquisa() beanUtilsBean e estes campos para realizar a limpeza por reflection
	 * @return
	 */
	public abstract String[] getCamposLimparPesquisa();
	
	/**
	 * Salva registro que est� sendo editado na p�gina
	 * 
	 * @return
	 * @throws JazzBusinessException
	 */
	public String actionSalvar() throws JazzBusinessException {
		if (getTmpVO() != null) {
			final IBusiness<B> business = this.getBusiness();
			
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
						
			return null;
		} else {
			
			return null;
		}
	}
	
	
	/**
	 * Parseia mensagens de erro de neg�cio. Normalmente registra uma mensagem JSF
	 * @param e
	 */
	protected void parseBusinessException(JazzBusinessException e) {
		
		if(log.isDebugEnabled()){
			log.warn("Erro de neg�cio",e);
		}

		String message = e.getMessage();
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, message,message));
		
	}



	/**
	 * Muda estado do jsfbean para que o modal de manutencao feche
	 */
	public void successSaveState(){
		//setTmpVO(null);
	}
	
	/**
	 * Converte as exce��es apontadas em valida��es autom�ticas em mensagens
	 * padr�o JSF
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
		String message = constraintViolation.getMessage();
		return new FacesMessage(message);
	}
	
	/**
	 * Retorna o nome do formul�rio com os campos de manuten��o
	 * @return
	 */
	protected String getFrmManutencao() {
		return "frmManutencao:";
	}
	
	
	
	/**
	 * Adiciona mensagem de di�logo para o usu�rio
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
 	 * Recupera a lista dos valores do enum informado
 	 * @param <E>
 	 * @param enumClass
 	 * @return
 	 */
 	public <E extends Enum<E>> List<E> getEnumsList(Class<E> enumClass){
 		
		EnumSet<E> enumSet = EnumSet.allOf(enumClass); 
 		
		return new ArrayList<E>(enumSet);
		
 	}
 	
	/**
	 * Retorna o BusinessObject da implementa��o concreta do m�dulo
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


	/**
	 * Retorna os grupos de valida��o para este m�dulo.
	 * @return
	 */
	public Set<String> getValidationGroups() {
		Set<String> validationGroups = new HashSet<String>(1);
		
		validationGroups.add("javax.validation.groups.Default");
		
		return validationGroups;
	}
	


	/**
	 * Retorna os grupos de valida��o para este m�dulo.
	 * @return
	 */
	public Set<String> getValidationGroupsSubModules() {
		return getValidationGroups();
	}
	
	
	
	
	
}
