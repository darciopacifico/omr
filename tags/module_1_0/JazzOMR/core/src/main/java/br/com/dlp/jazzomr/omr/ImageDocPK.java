/**
 * 
 */
package br.com.dlp.jazzomr.omr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;

import br.com.dlp.framework.pk.AbstractPK;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;

/**
 * Chave de imagem de documento de prova
 * 
 */
public class ImageDocPK extends AbstractPK {

	protected static final long serialVersionUID = -999758455424144641L;
	
	protected static final String EXAM_PK_PATTERN = "^(.*)-(.*)$";
	protected static final Pattern pattern = Pattern.compile(ImageDocPK.EXAM_PK_PATTERN);
	protected static final int GROUP_PARTICIPATION = 1;
	protected static final int GROUP_PAGE = 2;
	
	private Long participation;
	private Integer page;
	
	
	public ImageDocPK() {
	}
	
	/**
	 * @param participation
	 * @param page
	 */
	public ImageDocPK(Long id, Integer page) {
		super();
		this.participation = id;
		this.page = page;
	}
	
	/**
	 * 
	 * @param strPK
	 * @throws JazzOMRException 
	 */
	public ImageDocPK(String strPK) throws JazzOMRException{
		
		
		Matcher matcher = pattern.matcher(strPK);
		
		if(matcher.matches()){
			
			String strPar = matcher.group(ImageDocPK.GROUP_PARTICIPATION);
			String strPage = matcher.group(ImageDocPK.GROUP_PAGE);
			
			if(NumberUtils.isNumber(strPar)){
				setParticipation(Long.parseLong(strPar));
			}

			if(NumberUtils.isNumber(strPage)){
				setPage(Integer.parseInt(strPage));
			}
			
			
		}else{
			throw new JazzOMRException("Erro ao tentar interpretar contida no QRCode da imagem. Valor informado ="+strPK+" padrao esperado = "+ImageDocPK.EXAM_PK_PATTERN);
		}
		
	}
	
	/**
	 * @return the participation
	 */
	public Long getParticipation() {
		return participation;
	}
	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}
	/**
	 * @param participation the participation to set
	 */
	public void setParticipation(Long id) {
		this.participation = id;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}
	

	
	
}
