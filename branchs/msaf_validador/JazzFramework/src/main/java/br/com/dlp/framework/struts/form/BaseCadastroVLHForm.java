package br.com.dlp.framework.struts.form;

/**
 * Abstração de ActionForm
 * 
 * @author Darcio
 * 
 */
public abstract class BaseCadastroVLHForm extends BaseCadastroForm {
	private int paginaVLH;
	private int qtdPaginas;
	private int maxPagesOnView = 25;

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
}
