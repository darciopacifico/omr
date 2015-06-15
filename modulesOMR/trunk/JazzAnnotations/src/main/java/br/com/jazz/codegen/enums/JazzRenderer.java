/**
 * 
 */
package br.com.jazz.codegen.enums;

/**
 * Renderizadores padrão para uso com jazzannotations. Possuem composição com o enum de modelo de listagem
 * 
 * @author t_dpacifico
 * 
 */
public enum JazzRenderer {
	TEXT(Relacionamento.ATRIBUTO),
	TEXTAREA(Relacionamento.ATRIBUTO),
	WYSIWYG(Relacionamento.ATRIBUTO),
	CALENDAR(Relacionamento.ATRIBUTO),
	NUMBER_SPINNER(Relacionamento.ATRIBUTO),
	NUMBER_SLIDER(Relacionamento.ATRIBUTO),
	FILEUPLOAD(Relacionamento.ATRIBUTO),
	
	TEXT_SUGGESTION(DesignSolucao.PESQUISA_DINAMICA,Relacionamento.UM),
	POPUP(DesignSolucao.LISTAGEM_SESSAO,Relacionamento.UM),
	
	COMBO(DesignSolucao.LISTAGEM_SESSAO,Relacionamento.UM),
	RADIO(DesignSolucao.LISTAGEM_SESSAO,Relacionamento.UM),

	CHECKBOX(DesignSolucao.LISTAGEM_SESSAO,Relacionamento.MUITOS),
	PICKLIST(DesignSolucao.LISTAGEM_SESSAO,Relacionamento.MUITOS),
	
	LISTSHUTTLE(DesignSolucao.LISTAGEM_SESSAO,Relacionamento.MUITOS),
	
	
	GRID(DesignSolucao.INPLACE,Relacionamento.MUITOS),
	SUBMODULE(DesignSolucao.MODAL_EDITOR,Relacionamento.MUITOS),
	
	
	
	/**
	 * INFERIR CONFIGURACAO
	 */
	DEFAULT ;
	
	DesignSolucao designSolucao;
	Relacionamento relacionamento;
	
	
	private JazzRenderer() {
		designSolucao = DesignSolucao.NENHUM;
		relacionamento = Relacionamento.ATRIBUTO;
	}
	
	private JazzRenderer(DesignSolucao modeloListagem, Relacionamento cardinalidade) {
		designSolucao = modeloListagem;
		relacionamento = cardinalidade;
	}
	
	private JazzRenderer(DesignSolucao modeloListagem) {
		designSolucao = modeloListagem;
		relacionamento = Relacionamento.UM;
	}
	
	private JazzRenderer(Relacionamento cardinalidade) {
		designSolucao = DesignSolucao.NENHUM;
		relacionamento = cardinalidade;
	}
	
	public DesignSolucao getDesignSolucao() {
		return designSolucao;
	}
	
	public void setDesignSolucao(DesignSolucao designSolucao) {
		this.designSolucao = designSolucao;
	}
	
	public Relacionamento getRelacionamento() {
		return relacionamento;
	}
	
	public void setRelacionamento(Relacionamento relacionamento) {
		this.relacionamento = relacionamento;
	}
	
	public String getValue() {
		return toString();
	}
	
}
