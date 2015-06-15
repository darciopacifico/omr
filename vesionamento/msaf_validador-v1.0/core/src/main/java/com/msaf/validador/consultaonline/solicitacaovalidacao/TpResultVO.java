package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.msaf.validador.consultaonline.cliente.AbstractVO;

@Entity
public class TpResultVO extends AbstractVO implements Serializable {
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PK_GEN_TIPO_RESULTADO")
    @SequenceGenerator(name="PK_GEN_TIPO_RESULTADO", sequenceName="SEQUENCE_RESULTADO", allocationSize=1)
	@Column
	private Long id = new Long(0);

	@Column
	private String descricao = new String();

	private static final long serialVersionUID = 1L;

	@Transient
	public static final Long PROCESSADO_SUCESSO = new Long(10000);

	@Transient
	public static final Long PROCESSADO_ERRO =  new Long(10002);

	
	public TpResultVO() {
		super();
	}


	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
}
