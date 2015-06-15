package br.com.tsuite.tiporelacionamento;

/**
 * Enum para actions de CRUD básico
 * @author darcio
 *
 */
public enum CRUDActionEnum {
	NOVO		("novo"),
	SALVAR		("salvar"),
	ALTERAR		("alterar"),
	PESQUISAR	("pesquisar"),
	CANCELAR	("cancelar"),
	EXCLUIR		("excluir");

	private final String action;
	
	CRUDActionEnum(String action){
		this.action = action;
	}
	
	public String action(){return action;}
}
