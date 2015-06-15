package br.com.dlp.framework.popup;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.IValueListHandlerFacade;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;

public class VLHPopUpController extends PopUpController {
	private static final long serialVersionUID = 817420500149640745L;

	public static final String PAGINAR_VLH_NEXT_PAGE = "VLHPopUpController.vlhNextPage";

	public static final String PAGINAR_VLH_PREVIOUS_PAGE = "VLHPopUpController.vlhPreviousPage";

	public static final String PAGINAR_VLH_FIRST_PAGE = "VLHPopUpController.vlhFirstPage";

	public static final String PAGINAR_VLH_LAST_PAGE = "VLHPopUpController.vlhLastPage";

	private String paginarPara;

	private int paginaVLH;

	private int qtdPaginas;

	private int maxPagesOnView = 15;

	public int getMaxPagesOnView() {
		return maxPagesOnView;
	}

	public void setMaxPagesOnView(int maxPagesOnView) {
		this.maxPagesOnView = maxPagesOnView;
	}

	public int getFirstPageOnView() {
		maxPagesOnView = getMaxPagesOnView();
		int range = maxPagesOnView / 2;
		int paginaVLH = getPaginaVLH();
		int firstPageOnView = paginaVLH - range;
		int qtdPaginas = getQtdPaginas();

		if ((firstPageOnView + maxPagesOnView) > qtdPaginas) {
			firstPageOnView = qtdPaginas - maxPagesOnView;
		}

		if (firstPageOnView < 0) {
			firstPageOnView = 0;
		}

		return firstPageOnView;
	}

	public int getLastPageOnView() {
		maxPagesOnView = getMaxPagesOnView();
		int firstPageOnView = getFirstPageOnView();

		int lastPageOnView = firstPageOnView + maxPagesOnView;

		int qtdPaginas = getQtdPaginas();

		/* limita a ultima página do range */
		if (lastPageOnView > qtdPaginas) {
			lastPageOnView = qtdPaginas;
		}

		return lastPageOnView;
	}

	public int getQtdPaginas() {
		return qtdPaginas;
	}

	public void setQtdPaginas(int qtdPaginas) {
		this.qtdPaginas = qtdPaginas;
	}

	public int getPaginaVLH() {
		return paginaVLH;
	}

	public void setPaginaVLH(int paginaVLH) {
		this.paginaVLH = paginaVLH;
	}

	public VLHPopUpController(IValueListHandlerFacade valueListHandlerFacade,
			ResourceBundle resourceBundle, String servicoConfirmacao) {
		super(valueListHandlerFacade, servicoConfirmacao, resourceBundle);
	}

	public void executePopUpQuery() throws BaseBusinessException,
			BaseTechnicalError, RemoteException {

		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getFacade();
		String nomeFinderPesquisa = getNomeFinderPesquisa();

		if (nomeFinderPesquisa == null || nomeFinderPesquisa.trim().equals("")) {
			/*
			 * NENHUM SERVICO DE PESQUISA FOI ESPECIFICADO, VOU PEGAR O PADRAO,
			 * findAll
			 */
			valueListHandlerFacade.executeFindAll();

		} else {
			Object argumentValues[] = getArgumentValues();
			Class argumentTypes[] = getArgumentTypes();

			/* agrupo os argumentos */
			FrameworkAcheUtil.invocaMetodo(valueListHandlerFacade,
					nomeFinderPesquisa, argumentTypes, argumentValues);
		}

		List page = valueListHandlerFacade.getPage();

		int qtdPaginas = valueListHandlerFacade.getPages();
		int paginaAtual = valueListHandlerFacade.getPageIndex();

		setQtdPaginas(qtdPaginas);
		setPaginaVLH(paginaAtual);

		setPopUpItemVOs(page);

	}

	protected Object instanciarArgumento(Class tipo, String valor)
			throws FrameworkAcheUtilError {
		if (valor == null || valor.trim().equals("")) {
			if (tipo.isInstance(((Number) new Long(0)))) {
				valor = "0";
			} else if (tipo.isInstance("")) {
				valor = "";
			} else if (tipo.isInstance(new Date())) {
				valor = "";
			} else {
				valor = "";
			}
		}

		Object retorno = FrameworkAcheUtil.instanciaObjeto(tipo,
				new Class[] { String.class }, new Object[] { valor });

		return retorno;

	}

	public void processarPaginacao() throws BaseTechnicalError, RemoteException {
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getFacade();

		String paginarPara = getPaginarPara();

		List page = null;

		if (paginarPara == null || paginarPara.trim().equals("")) {

		} else if (paginarPara.equals(PAGINAR_VLH_FIRST_PAGE)) {
			page = valueListHandlerFacade.getFirstPage();

		} else if (paginarPara.equals(PAGINAR_VLH_LAST_PAGE)) {
			page = valueListHandlerFacade.getLastPage();

		} else if (paginarPara.equals(PAGINAR_VLH_NEXT_PAGE)) {
			page = valueListHandlerFacade.getNextPage();

		} else if (paginarPara.equals(PAGINAR_VLH_PREVIOUS_PAGE)) {
			page = valueListHandlerFacade.getPreviousPage();

		} else {
			int index = new Integer(paginarPara).intValue();
			page = valueListHandlerFacade.getPage(index);

		}

		int paginaReal = valueListHandlerFacade.getPageIndex();

		setPaginaVLH(paginaReal);

		setPopUpItemVOs(page);
	}

	public String getPaginarPara() {
		return paginarPara;
	}

	public void setPaginarPara(String paginarPara) {
		this.paginarPara = paginarPara;
	}

}