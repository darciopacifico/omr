/**
 * 
 */
package br.com.dlp.jazzomr.person;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;

import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;
import br.com.jazz.codegen.enums.JazzRenderer;

import com.google.gson.annotations.Expose;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(name="Grupo",baseEntity=true)
/*
 * */
@FetchProfiles(value={
		@FetchProfile(name="grupo_com_pessoas", fetchOverrides={
				@FetchProfile.FetchOverride(entity=GrupoVO.class, association="pessoas", mode=FetchMode.JOIN),
		}),
})  
public class GrupoVO extends AbstractLogEntityVO  {

	private static final long serialVersionUID = -2978091007554618301L;
	
	private String description;
	
	@Expose(deserialize=false,serialize=false)
	private List<PessoaVO> pessoas = new ArrayList<PessoaVO>(40);
	
	
	public GrupoVO() {
	}

	/**
	 * @param pk
	 */
	public GrupoVO(Long pk) {
		super(pk);
	}

	/**
	 * @return the description
	 */
	@Column(length=200)
	@JazzProp(name="Descrição",comparison=ComparisonOperator.LIKE,sortable=true,tip="Descrição do Grupo")
	public String getDescription() {
		return description;
	}

	/**
	 * @return the pessoas
	 */
	@JazzProp(name="Pessoas",renderer=JazzRenderer.LISTSHUTTLE,listable=false, searchable=false)
	@ManyToMany
	public List<PessoaVO> getPessoas() {
		return pessoas;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param pessoas the pessoas to set
	 */
	public void setPessoas(List<PessoaVO> pessoas) {
		this.pessoas = pessoas;
	}

	
	
	
}
