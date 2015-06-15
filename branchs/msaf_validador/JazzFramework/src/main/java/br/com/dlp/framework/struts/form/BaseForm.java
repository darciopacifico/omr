package br.com.dlp.framework.struts.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.popup.PopUpArgument;
import br.com.dlp.framework.popup.PopUpController;
import br.com.dlp.framework.struts.action.BaseAction;
import br.com.dlp.framework.struts.action.BaseCadastroAction;
import br.com.dlp.framework.vo.PopUpItemVO;

public abstract class BaseForm extends ActionForm {
	public static final String ACAOESTADO_INCLUSAO = "acaoEstadoInclusao";

	public static final String ACAOESTADO_ALTERACAO = "acaoEstadoAlteracao";

	public static final String ACAOESTADO_CONSULTA = "acaoEstadoConsulta";

	public static final String ACAOESTADO_PESQUISA = "acaoEstadoPesquisa";

	private String paramService = null;

	private String acaoEstado = null;

	private String ultimoForward;

	private String synchronizedToken;

	private PopUpController popUpController;

	/* guarda os títulos das telas percorridas para chegar até a tela atual */
	private List tituloCaminhoForwards = new ArrayList(0);

	protected Log logger = LogFactory.getLog(BaseCadastroForm.class);

	/* registra os forwards de destino para os eventos voltar/fechar/salvar */
	private Map caminhoForwards = new HashMap(3);

	/**
	 * @return Returns the argument.
	 */
	public PopUpArgument getArgument(int index) {
		return (PopUpArgument) popUpController.getArguments().get(index);
	}

	/**
	 * @param argument
	 *            The argument to set.
	 */
	public void setArgument(int index, PopUpArgument argument) {
		this.popUpController.getArguments().set(index, argument);
	}

	public String getAcaoEstado() {
		return acaoEstado;
	}

	public void setAcaoEstado(String acaoEstado) {
		this.acaoEstado = acaoEstado;
	}

	public String getParamService() {
		return paramService;
	}

	/**
	 * Configura o parametro de servico a ser chamado e o estado da aplicação
	 */
	public void setParamService(String paramService) {
		if (paramService == null) {
			paramService = "";
		}

		if (paramService.equals(BaseCadastroAction.SERVICE_PREPARA_ALTERAR)) {
			acaoEstado = ACAOESTADO_ALTERACAO;

		} else if (paramService
				.equals(BaseCadastroAction.SERVICE_PREPARA_INCLUIR)) {
			acaoEstado = ACAOESTADO_INCLUSAO;

		} else if (paramService
				.equals(BaseCadastroAction.SERVICE_PREPARA_CONSULTAR)) {
			acaoEstado = ACAOESTADO_CONSULTA;

		}
		this.paramService = paramService;
	}

	public void setPopUpItemVO(int index, PopUpItemVO popUpItemVO) {
		popUpController.getPopUpItemVOs().set(index, popUpItemVO);
	}

	public PopUpItemVO getPopUpItemVO(int index) {
		PopUpItemVO popUpItemVO = (PopUpItemVO) popUpController
				.getPopUpItemVOs().get(index);
		return popUpItemVO;
	}

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);

		/* reseta check box de popups */
		PopUpController popUpController = getPopUpController();
		if (popUpController != null) {
			List popUpItemVOs = popUpController.getPopUpItemVOs();
			if (popUpItemVOs != null) {
				Iterator iterator = popUpItemVOs.iterator();
				while (iterator.hasNext()) {
					PopUpItemVO popUpItemVO = (PopUpItemVO) iterator.next();
					popUpItemVO.setChecked(false);
				}
			}
		}

	}

	public PopUpController getPopUpController() {
		return popUpController;
	}

	public void setPopUpController(PopUpController popUpController) {
		this.popUpController = popUpController;
	}

	/**
	 * Recupera o valor de uma propriedade do form de acordo com o padrão
	 * nestedProperty public Object getNestedProperty(String nestedProperty,
	 * boolean ignore, Object defaultValue) throws ActionFormException { Object
	 * object = null; try {
	 * 
	 * PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); object =
	 * propertyUtilsBean.getNestedProperty(this, nestedProperty);
	 *  } catch (NestedNullException e) {
	 *  // testa se devo ignorar a exceção NestedNullException if (ignore ==
	 * true) { object = defaultValue; } else { throw e; }
	 *  } catch (IllegalAccessException e) { throw new ActionFormException("Nao
	 * foi possivel recuperar a propriedade (nested) " + nestedProperty + "!");
	 *  } catch (InvocationTargetException e) { throw new
	 * ActionFormException("Nao foi possivel recuperar a propriedade (nested) " +
	 * nestedProperty + "!");
	 *  } catch (NoSuchMethodException e) { throw new ActionFormException("Nao
	 * foi possivel recuperar a propriedade (nested) " + nestedProperty + "!");
	 *  } return object; }
	 */

	/**
	 * Sobrecaga para chamada padrao com ignore==true e defaultValue==null
	 * public Object getNestedProperty(String nestedProperty) throws
	 * ActionFormException { return getNestedProperty(nestedProperty, true,
	 * null); }
	 */

	/**
	 * Sobrecaga para chamada padrao com ignore==true e defaultValue==null
	 * public Object getNestedProperty(String nestedProperty, Object
	 * defaultValue) throws ActionFormException { return
	 * getNestedProperty(nestedProperty, true, defaultValue); }
	 */

	/**
	 * Atribui um valor aa uma propriedade do form de acordo com o padrão
	 * nestedProperty public void setNestedProperty(String nestedProperty,
	 * Object valor) throws ActionFormException {
	 * 
	 * try { PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
	 * propertyUtilsBean.setNestedProperty(this, nestedProperty, valor); } catch
	 * (IllegalAccessException e) { throw new ActionFormException("Nao foi
	 * possivel atribuir a lista à propriedade (nested) " + nestedProperty +
	 * "!", e);
	 *  } catch (InvocationTargetException e) { throw new
	 * ActionFormException("Nao foi possivel atribuir a lista à propriedade
	 * (nested) " + nestedProperty + "!", e);
	 *  } catch (NoSuchMethodException e) { throw new ActionFormException("Nao
	 * foi possivel atribuir a lista à propriedade (nested) " + nestedProperty +
	 * "!", e);
	 *  } }
	 */

	/**
	 * @return Returns the ultimoForward.
	 */
	public String getUltimoForward() {
		return ultimoForward;
	}

	/**
	 * @param ultimoForward
	 *            The ultimoForward to set.
	 */
	public void setUltimoForward(String ultimoForward) {
		this.ultimoForward = ultimoForward;
	}

	public Map getCaminhoForwards() {
		return caminhoForwards;
	}

	public ResourceBundle getResourceBundle(ActionMapping mapping,
			HttpServletRequest request) {
		ResourceBundle resourceBundle = (ResourceBundle) request.getSession()
				.getAttribute(BaseAction.PARAM_RESOURCE_BUNDLE);

		return resourceBundle;
	}

	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		/* Retorna apenas ActionErrors vazio */
		return new ActionErrors();
	}

	public String getSynchronizedToken() {
		return synchronizedToken;
	}

	public void setSynchronizedToken(String synchronizedToken) {
		this.synchronizedToken = synchronizedToken;
	}

	/**
	 * Caminho da tela atual (ex: Manter Pedidos\Itens de Pedido\Histórico de
	 * Itens de Pedido)
	 */
	public List getTituloCaminhoForwards() {
		return tituloCaminhoForwards;
	}

	/**
	 * Caminho da tela atual (ex: Manter Pedidos\Itens de Pedido\Histórico de
	 * Itens de Pedido)
	 */
	public void setTituloCaminhoForwards(List tituloCaminhoForwards) {
		this.tituloCaminhoForwards = tituloCaminhoForwards;
	}

}
