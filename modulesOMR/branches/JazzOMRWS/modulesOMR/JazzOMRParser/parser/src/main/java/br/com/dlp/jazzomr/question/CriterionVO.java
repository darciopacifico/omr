package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;


/**
 * Tipo abstrato de criterio para questoes. Os criterios concretos podem ser alternativos ou dissertativos. 
 * Uma questão pode possuir ambos os tipos
 * 
 * @author darcio
 */
public class CriterionVO extends AbstractBaseVO<Long> implements ISortable {

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
	@Enumerated(EnumType.ORDINAL)
	@JazzProp(name="Score",renderer=JazzRenderer.RADIO)
	public AlternativeScore getScore() {
		return score;
	}

	@Transient
	@XmlTransient
	public Boolean getCorrect(){
		return AlternativeScore.CORRECT.equals(score);
	}
	
	@Transient	
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
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	@Transient
	@XmlTransient
	public Boolean getIsDissertative(){
		return "D".equals(critType);
	}

	@Transient
	@XmlTransient
	public Boolean getIsAlternative(){
		return "A".equals(critType);
	} 

	/**
	 * @return the resumo
	 */
	@JazzProp(name = "Resumo", renderer = JazzRenderer.TEXTAREA)
	@Column(length = 500)
	@Length(max=500,message="A descrição de um critério alternativo ou dissertativo deve ter no máximo 500 caracteres!")
	@NotEmpty(message = "Por favor, preencha a descrição de todas as alternativas e critérios dissertativos!")
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
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
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
