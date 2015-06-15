package br.com.dlp.jazzqa.pessoa;


import javax.persistence.Column;
import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;

/**
 * Cadastro básico de pessoa, modelo de referencia para o gerador de código e framework
 * @author dpacifico
 *
 */
@Entity
public class PessoaVO extends AbstractBaseVO<PessoaPK> {
	private static final long serialVersionUID = 8541646026789968351L;

	@Column
	private String descricao;

	public PessoaVO(Long i) {
		super(new PessoaPK(i));
	}

	public PessoaVO() {
		super();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
