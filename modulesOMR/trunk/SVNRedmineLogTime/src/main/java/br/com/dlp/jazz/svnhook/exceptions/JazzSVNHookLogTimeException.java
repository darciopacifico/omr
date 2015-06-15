/**
 * 
 */
package br.com.dlp.jazz.svnhook.exceptions;

import br.com.dlp.jazz.svnhook.vos.CitationVO;

/**
 * Erro de negócio ao tentar efetuar o logtime
 * @author t_dpacifico
 */
public class JazzSVNHookLogTimeException extends JazzSVNHookException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3906853842686251574L;
	private CitationVO citacaoIssueVO;

	/**
	 * construtor padrao
	 */
	public JazzSVNHookLogTimeException() {
		//explicity empty
	}

	/**
	 * @param arg0
	 */
	public JazzSVNHookLogTimeException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public JazzSVNHookLogTimeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public JazzSVNHookLogTimeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param citacaoIssueVO
	 * @param message
	 */
	public JazzSVNHookLogTimeException(CitationVO citacaoIssueVO, String message) {
		super(message);
		this.citacaoIssueVO = citacaoIssueVO;
	}

	/**
	 * @return the citacaoIssueVO
	 */
	public CitationVO getCitacaoIssueVO() {
		return citacaoIssueVO;
	}

	/**
	 * @param citacaoIssueVO the citacaoIssueVO to set
	 */
	public void setCitacaoIssueVO(CitationVO citacaoIssueVO) {
		this.citacaoIssueVO = citacaoIssueVO;
	}

	
	
}
