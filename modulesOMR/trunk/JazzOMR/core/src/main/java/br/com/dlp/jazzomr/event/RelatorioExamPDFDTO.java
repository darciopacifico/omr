/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import br.com.dlp.jazzomr.empresa.RelatorioVO;

/**
 * @author darcio
 *
 */
public class RelatorioExamPDFDTO implements Serializable {

	private static final long serialVersionUID = 7299541481487783316L;
	
	//TODO: internacionalizar
	protected static final DateFormat FILENAME_DTFORMAT = new SimpleDateFormat("ddMMyy_HHmm");
	
//TODO: internacionalizar
	protected static final String PDF_FILE_NAME_PATTERN = "ExamsEvt_{0}_{1}_{2}.pdf";

	//TODO: internacionalizar
	protected static final String ZIP_FILE_NAME_PATTERN = "ExamsEvt_{0}_{1}.zip";
	
	private byte[] bytesPDF;
	private RelatorioVO relatorioVO;
	private EventVO eventVO;

	
	/**
	 * 
	 */
	public RelatorioExamPDFDTO() {
	}

	public byte[] getBytesPDF() {
		return bytesPDF;
	}

	public RelatorioVO getRelatorioVO() {
		return relatorioVO;
	}

	public EventVO getEventVO() {
		return eventVO;
	}

	public void setBytesPDF(byte[] bytesPDF) {
		this.bytesPDF = bytesPDF;
	}

	public void setRelatorioVO(RelatorioVO relatorioVO) {
		this.relatorioVO = relatorioVO;
	}

	public void setEventVO(EventVO eventVO) {
		this.eventVO = eventVO;
	}

	/**
	 * Formata nome do arquivo
	 * @param eventVO
	 * @param fileNamePattern TODO
	 * @return
	 */
	protected String getPDFFileName() {
		//TODO internacionalizar nomes
		
		String strData = FILENAME_DTFORMAT.format(eventVO.getDtAlt());
		
		String pattern = PDF_FILE_NAME_PATTERN;
		
		Long eventPK = eventVO.getPK();
		Long relatorioPK = relatorioVO.getPK();

		String fileName = MessageFormat.format(pattern, eventPK,relatorioPK,strData);
		
		return fileName;
	}

	
	

	
	/**
	 * Formata nome do arquivo
	 * @param eventVO
	 * @param fileNamePattern TODO
	 * @return
	 */
	public static String getZIPFileName(EventVO eventVO) {
		//TODO internacionalizar nomes
		
		String strData = FILENAME_DTFORMAT.format(eventVO.getDtAlt());
		
		String pattern = ZIP_FILE_NAME_PATTERN;
		
		Long eventPK = eventVO.getPK();

		String fileName = MessageFormat.format(pattern, eventPK,strData);
		
		return fileName;
	}

	
	

}
