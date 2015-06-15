/**
 * 
 */
package br.com.dlp.jazzomr.empresa;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.dlp.jazzomr.base.AbstractLogEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Registro de empresa
 * @author darcio
 */
@Entity
@JazzClass(name="Empresa")

/*
@FetchProfiles(value={
		@FetchProfile(name="empresa_com_logos", fetchOverrides={
				@FetchProfile.FetchOverride(entity=EmpresaVO.class	, association="logos", mode=FetchMode.JOIN)
		}),
})  
*/
public class EmpresaVO extends AbstractLogEntityVO<Long> {
	private static final long serialVersionUID = 1L;

	private String nome;
	private String descricao;
	
	@NotBlank(message="Desc da empresa não pode ficar em branco")
	@JazzProp(name = "desc da Empresa")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
	public EmpresaVO() { 
	}

	public EmpresaVO(Long pk) {
		super(pk);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	
	@NotBlank(message="Nome da empresa não pode ficar em branco")
	@Length(min=10,max=50)
	@JazzProp(name = "Nome da Empresa")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}