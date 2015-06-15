package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;


/**
 * Tipo abstrato de criterio para questoes. Os criterios concretos podem ser alternativos ou dissertativos. 
 * Uma questão pode possuir ambos os tipos
 * 
 * @author darcio
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(	
			name = "critType",
			discriminatorType = DiscriminatorType.STRING
		)
public abstract class AbstractCriterionVO extends AbstractLogEntityVO implements ISortable {

	private static final long serialVersionUID = 7063523268969921212L;
	
	protected String description;
	
	private List<ImageVO> imageVOs = new ArrayList<ImageVO>(2);
	
	private Double ordem; 
	
	public AbstractCriterionVO() {
		super();
	}

	public AbstractCriterionVO(Long pk) {
		super(pk);
	}
	
	@Transient
	public Boolean getIsDissertative(){
		return this instanceof DissertativeVO;
	} 

	@Transient
	public Boolean getIsAlternative(){
		return this instanceof AlternativeVO;
	} 

	/**
	 * @return the resumo
	 */
	@JazzProp(name = "Resumo", renderer = JazzRenderer.TEXTAREA)
	@Column(length = 200)
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
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
	public List<ImageVO> getImageVOs() {
		return imageVOs;
	}	

	public void setImageVOs(List<ImageVO> imageVOs) {
		this.imageVOs = imageVOs;
	}	

	
	@Override
	public boolean equals(Object arg) {
		// TODO Auto-generated method stub
		return super.equals(arg);
	}

	public Double getOrdem() {
		return ordem;
	}

	public void setOrdem(Double ordem) {
		this.ordem = ordem;
	}
	
}
