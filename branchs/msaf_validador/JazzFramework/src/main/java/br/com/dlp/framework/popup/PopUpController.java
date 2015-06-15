package br.com.dlp.framework.popup;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.ConvertUtilsBean;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.ICadastroFacade;
import br.com.dlp.framework.facade.IFacade;
import br.com.dlp.framework.struts.action.BaseAction;
import br.com.dlp.framework.util.AcheCollectionUtils;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.vo.PopUpItemVO;

public class PopUpController implements Serializable {
	private static final long serialVersionUID = 4687999535281305947L;

	private String nomeFinderPesquisa;

	private List arguments;

	private IFacade facade;

	private boolean multiplaEscolha = false;

	private boolean pesquisa = false;

	private String servicoDepoisDoPopUp = null;

	private String servicoRefreshPopUp = BaseAction.SERVICE_PESQUISA_POPUP;

	private String popUpForward = null;

	private List popUpItemVOs = new ArrayList();

	private List checkedItens = new ArrayList();

	private Object checkedItem = null;

	private String tituloPopUp = "";

	private ResourceBundle resourceBundle;

	/* ATRIBUTOS DO MECANISMO DO POPUP */

	public PopUpController(IFacade facade, String servicoConfirmacao,
			ResourceBundle resourceBundle) {

		setFacade(facade);
		setServicoDepoisDoPopUp(servicoConfirmacao);
		setResourceBundle(resourceBundle);

	}

	/**
	 * Logica para invocar pesquisa no popup
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public void executePopUpQuery() throws BaseBusinessException,
			BaseTechnicalError, RemoteException {
		ICadastroFacade cadastroFacade;
		try {
			cadastroFacade = (ICadastroFacade) getFacade();
		} catch (ClassCastException e) {
			throw new BaseTechnicalError("", e);
		}
		String nomeFinderPesquisa = getNomeFinderPesquisa();

		List list;

		if (nomeFinderPesquisa == null || nomeFinderPesquisa.trim().equals("")) {
			/*
			 * NENHUM SERVICO DE PESQUISA FOI ESPECIFICADO, VOU PEGAR O PADRAO,
			 * findAll
			 */
			list = cadastroFacade.findAll();

		} else {
			Object argumentValues[] = getArgumentValues();
			Class argumentTypes[] = getArgumentTypes();

			/* agrupo os argumentos */
			list = (List) FrameworkAcheUtil.invocaMetodo(cadastroFacade,
					nomeFinderPesquisa, argumentTypes, argumentValues);
		}

		setPopUpItemVOs(list);

	}

	/**
	 * Retorna um único item selecionado no popUp
	 */
	public Object getUniquePopUpItem() {
		Iterator iterator = popUpItemVOs.iterator();

		while (iterator.hasNext()) {
			PopUpItemVO popUpItemVO = (PopUpItemVO) iterator.next();
			if (popUpItemVO.isChecked()) {
				Object value = popUpItemVO.getValue();
				return value;
			}
		}
		return null;
	}

	/**
	 * Retorna os itens que foram selecionados no PopUp
	 * 
	 * @throws PopUpControllerException
	 */
	public List getCheckedPopUpItens() throws PopUpControllerException {
		if (!isMultiplaEscolha()) {
			throw new PopUpControllerException(
					"Não é possível recuperar o valor da seleção do popUp através deste método quando o PopUpController é para escolha única.");
		}

		List returnList = new ArrayList();
		Iterator iterator = popUpItemVOs.iterator();

		while (iterator.hasNext()) {
			PopUpItemVO popUpItemVO = (PopUpItemVO) iterator.next();
			if (popUpItemVO.isChecked()) {
				Object value = popUpItemVO.getValue();
				returnList.add(value);
			}
		}
		return returnList;
	}

	/**
	 * @throws PopUpControllerException
	 * 
	 */
	public Object getCheckedPopUpItem() throws PopUpControllerException {

		if (isMultiplaEscolha()) {
			throw new PopUpControllerException(
					"Não é possível recuperar o valor da seleção do popUp através deste método quando o PopUpController é para múltipla escolha");
		}

		Iterator iterator = popUpItemVOs.iterator();

		while (iterator.hasNext()) {
			PopUpItemVO popUpItemVO = (PopUpItemVO) iterator.next();
			if (popUpItemVO.isChecked()) {
				Object value = popUpItemVO.getValue();
				return value;
			}
		}
		return null;
	}

	/**
	 * Inclui no formulario os itens para o popup, verificando quais devem
	 * apacerecer como checados
	 */
	protected void setPopUpItemVOs(List popUpItemVOs, List checkedObjects) {
		List listPopUpItemVOs = new ArrayList();

		Iterator iterator = popUpItemVOs.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			/**
			 * verifica se este item está nos checados
			 */
			boolean contains = AcheCollectionUtils.contains(checkedObjects,
					object);
			listPopUpItemVOs.add(new PopUpItemVO(object, contains));
		}
		this.popUpItemVOs = listPopUpItemVOs;
	}

	protected void setPopUpItemVOs(List popUpItemVOs, Object checkedObject) {
		List listPopUpItemVOs = new ArrayList();

		Iterator iterator = popUpItemVOs.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			/**
			 * verifica se este item está nos checados
			 */

			boolean contains = false;
			if (checkedObject != null) {
				contains = object.equals(checkedObject);
			}

			listPopUpItemVOs.add(new PopUpItemVO(object, contains));
		}
		this.popUpItemVOs = listPopUpItemVOs;
	}

	/**
	 * Inclui no formulario os itens para o popup
	 */
	protected void setPopUpItemVOs(List popUpItemVOs) {

		if (isMultiplaEscolha()) {
			setPopUpItemVOs(popUpItemVOs, getCheckedItens());
		} else {
			setPopUpItemVOs(popUpItemVOs, getCheckedItem());
		}

	}

	public List getPopUpItemVOs() {
		return popUpItemVOs;
	}

	/**
	 * Retorna o popUpForward configurado. Caso este seja nulo retorna o popUp
	 * forward padrao
	 */
	public String getPopUpForward() {
		if (popUpForward == null) {
			return BaseAction.FORWARD_GLOBAL_POPUP;
		}
		return popUpForward;
	}

	public void setPopUpForward(String popUpForward) {
		this.popUpForward = popUpForward;
	}

	public String getServicoDepoisDoPopUp() {
		return servicoDepoisDoPopUp;
	}

	public void setServicoDepoisDoPopUp(String servicoDepoisDoPopUp) {
		this.servicoDepoisDoPopUp = servicoDepoisDoPopUp;
	}

	public String getTituloPopUp() {
		return tituloPopUp;
	}

	public void setTituloPopUp(String tituloPopUp) {
		this.tituloPopUp = tituloPopUp;
	}

	public boolean isMultiplaEscolha() {
		return multiplaEscolha;
	}

	public boolean getMultiplaEscolha() {
		return multiplaEscolha;
	}

	public void setMultiplaEscolha(boolean multiplaEscolha) {
		this.multiplaEscolha = multiplaEscolha;
	}

	public boolean isPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(boolean pesquisa) {
		this.pesquisa = pesquisa;
	}

	public List getCheckedItens() {
		return checkedItens;
	}

	public void setCheckedItens(List checkedItens)
			throws PopUpControllerException {
		if (!multiplaEscolha) {
			throw new PopUpControllerException(
					"Não é possível indicar uma colecao de itens para serem checados em um PopUpCotroller de escolha única");
		}
		this.checkedItens = checkedItens;
	}

	public Object getCheckedItem() {
		return checkedItem;
	}

	public void setCheckedItem(Object checkedItem)
			throws PopUpControllerException {
		if (multiplaEscolha) {
			throw new PopUpControllerException(
					"Não é possível indicar um único objeto como objeto checado à um PopUpController de escolha múltipla");
		}
		this.checkedItem = checkedItem;
	}

	public String getServicoRefreshPopUp() {
		return servicoRefreshPopUp;
	}

	public void setServicoRefreshPopUp(String servicoRefreshPopUp) {
		this.servicoRefreshPopUp = servicoRefreshPopUp;
	}

	/**
	 * @return Returns the arguments.
	 */
	public List getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            The arguments to set.
	 */
	public void setArguments(List arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return Returns the argumentTypes.
	 */
	protected Class[] getArgumentTypes() {
		Class[] argumentTypes = new Class[arguments.size()];

		Iterator iterator = arguments.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			PopUpArgument popUpArgument = (PopUpArgument) iterator.next();
			argumentTypes[i++] = popUpArgument.getArgumentType();
		}

		return argumentTypes;
	}

	/**
	 * @return Returns the argumentValues.
	 * @throws PopUpControllerException
	 */
	protected Object[] getArgumentValues() throws PopUpControllerException {
		Object[] argumentValues = new Object[arguments.size()];

		Iterator iterator = arguments.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			PopUpArgument popUpArgument = (PopUpArgument) iterator.next();

			/* valor em formato String */
			String value = popUpArgument.getValue();
			/* tipo do valor */
			Class type = popUpArgument.getArgumentType();

			/* CRIANDO UM OBJETO ConvertUtilsBean */
			ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

			/* Configura um ConvertUtilsBean já criado */
			FrameworkAcheUtil.configureConvertUtilsBean(convertUtilsBean,
					getResourceBundle());

			/* INVOCANDO CONVERSÃO */
			Object objectValue = convertUtilsBean.convert(value, type);

			/* ATRIBUINDO AA LISTA DE ARGUMENTOS */
			argumentValues[i++] = objectValue;
		}

		return argumentValues;
	}

	/**
	 * @return Returns the nomeFinderPesquisa.
	 */
	public String getNomeFinderPesquisa() {
		return nomeFinderPesquisa;
	}

	/**
	 * @param nomeFinderPesquisa
	 *            The nomeFinderPesquisa to set.
	 */
	public void setNomeFinderPesquisa(String nomeFinderPesquisa) {
		this.nomeFinderPesquisa = nomeFinderPesquisa;
	}

	public String toString() {
		return getTituloPopUp();
	}

	public void finalizarPopUp() throws PopUpControllerException {
		try {
			IFacade facade = getFacade();
			facade.finishFacade();

		} catch (BaseTechnicalError e) {
			throw new PopUpControllerException("Erro ao finalizar popup!!", e);

		} catch (RemoteException e) {
			throw new PopUpControllerException("Erro ao finalizar popup!!", e);

		}
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public IFacade getFacade() {
		return facade;
	}

	public void setFacade(IFacade facade) {
		this.facade = facade;
	}

}
