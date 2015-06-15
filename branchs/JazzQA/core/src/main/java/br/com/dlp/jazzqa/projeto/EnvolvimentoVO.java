package br.com.dlp.jazzqa.projeto;
import javax.persistence.Entity;

import br.com.dlp.jazzqa.base.AbstractEntityVO;
import br.com.dlp.jazzqa.pessoa.PessoaVO;
import br.com.dlp.jazzqa.projeto.enums.PapelEnum;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzTextArea;
import br.com.jazz.codegen.enums.JazzRenderer;

/**
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:16
 */
@Entity
@JazzClass(name="Envolvido", description="Pessoa envolvida em projeto de TI, com papel e descrição!",voMestre=ProjetoVO.class)
public class EnvolvimentoVO extends AbstractEntityVO{
	
	private static final long serialVersionUID = 2820218459756027422L;
	
	
	private String descricao;
	private PessoaVO pessoa;
	private PapelEnum papel;
	
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public EnvolvimentoVO(){
		
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk    pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public EnvolvimentoVO(final Long pk){
		super(pk);
	}
	
	
	/**
	 * @return
	 */
	@JazzProp(name="Descrição", renderer=JazzRenderer.TEXTAREA,size="500")
	@JazzTextArea(cols=30, rows=4)
	public String getDescricao() {
		return descricao;
	}
	
	@JazzProp(name="Papel", renderer=JazzRenderer.POPUP, tip="Papel desempenhado pelo participante do projeto.")
	public PapelEnum getPapel() {
		return papel;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setPapel(PapelEnum papel) {
		this.papel = papel;
	}
	
	@JazzProp(name="Pessoa", tip="Pessoa envolvida no projeto!")
	public PessoaVO getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}
	
}