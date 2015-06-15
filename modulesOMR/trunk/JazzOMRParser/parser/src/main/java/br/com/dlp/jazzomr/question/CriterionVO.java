package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.BooleanUtils;

import br.com.dlp.jazzomr.AbstractBaseVO;
import br.com.dlp.jazzomr.results.ImageVO;


/**
 * Tipo abstrato de criterio para questoes. Os criterios concretos podem ser alternativos ou dissertativos. 
 * Uma questão pode possuir ambos os tipos
 * 
 * @author darcio
 */
public class CriterionVO extends AbstractBaseVO<Long> {

	private static final long serialVersionUID = 7063523268969921212L;
	
	protected String description;
	
	private List<ImageVO> imageVOs = new ArrayList<ImageVO>(2);
	
	private Double ordem; 
	
	private Integer qtdLinhas=5;
	private Boolean required=true;
	private Integer maxPonto=1;
	
	private String critType;
	

	private AlternativeScore score = AlternativeScore.WRONG;

	@Override
	public void setPK(Long ppk) {
		// TODO Auto-generated method stub
		super.setPK(ppk);
	}
	
	/**
	 * @return the score
	 */
	public AlternativeScore getScore() {
		return score;
	}

	@XmlTransient
	public Boolean getCorrect(){
		return AlternativeScore.CORRECT.equals(score);
	}
	
	public void setCorrect(Boolean correct){
		
		if(BooleanUtils.isTrue(correct)){
			setScore(AlternativeScore.CORRECT);
		}else{
			setScore(AlternativeScore.WRONG);
		}
		
	}
	
	public Integer getQtdLinhas() {
		return qtdLinhas;
	}
	public Boolean getRequired() {
		return required;
	}

	public void setQtdLinhas(Integer linhasDescricao) {
		this.qtdLinhas = linhasDescricao;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Integer getMaxPonto() {
		return maxPonto;
	}

	public void setMaxPonto(Integer maxPontos) {
		this.maxPonto = maxPontos;
	}

	
	/**
	 * @param score
	 *          the score to set
	 */
	public void setScore(AlternativeScore score) {
		this.score = score;
	}
	

	public CriterionVO() {
		super();
	}

	public CriterionVO(Long pk) {
		super(pk);
	}
	
	
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	@XmlTransient
	public Boolean getIsDissertative(){
		return "D".equals(critType);
	}

	@XmlTransient
	public Boolean getIsAlternative(){
		return "A".equals(critType);
	} 

	/**
	 * @return the resumo
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param resumo
	 *          the resumo to set
	 */
	public void setDescription(String resumo) {
		this.description = resumo;
	}

	
	/**
	 * @return the imageVOs
	 */
	public List<ImageVO> getImageVOs() {
		return imageVOs;
	}	

	public void setImageVOs(List<ImageVO> imageVOs) {
		this.imageVOs = imageVOs;
	}	

	public Double getOrdem() {
		return ordem;
	}

	public void setOrdem(Double ordem) {
		this.ordem = ordem;
	}
	


	public void setCritType(String critType) {
		this.critType = critType;
	}

	public String getCritType() {
		return critType;
	}
	
}
