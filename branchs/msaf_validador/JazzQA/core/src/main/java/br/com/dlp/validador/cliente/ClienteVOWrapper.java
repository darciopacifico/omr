package br.com.dlp.validador.cliente;

import java.util.ResourceBundle;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementacao de IVOWrapper para ClienteVO
 **/
public class ClienteVOWrapper extends br.com.dlp.framework.vowrapper.AbstractVOWrapper{
	/**
	 * Controle de versão para classes serializáveis
	 */
	private static final long serialVersionUID = 907976127239847L;

	/*********************************/
	/*ATRIBUTOS PARA INVOCAR PESQUISA*/

	private java.lang.Integer pesquisaId;

	private java.lang.String pesquisaCnpj;

	private java.lang.String pesquisaNome;

	/*ATRIBUTOS PARA INVOCAR PESQUISA*/
	/*********************************/

	/**
	 * Construtor de 'ClienteVOWrapper' que recebe o 
	 * ResourceBundle contendo o padrão de formatação de números e datas internacionalizado
	 */
	public ClienteVOWrapper(ResourceBundle resourceBundle){
		super(resourceBundle);
	}

	/**
	 * Recupera o 'ClienteVO' encapsulado
	 */
	public ClienteVO getClienteVO(){
		return (ClienteVO)getBaseVO();
	}

	/****************************************************/
	/*GETTER E SETTER DE ATRIBUTOS PARA INVOCAR PESQUISA*/

	/**
	 * Converte e RETORNA o atributo 'pesquisaId' de 'java.lang.Integer' para 'String'
	 */
  public String getPesquisaId() {
    String str = convert(this.pesquisaId);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaId' de 'String' para 'java.lang.Integer' 
   */
  public void setPesquisaId(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    this.pesquisaId = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaId' como 'Integer'
	 */
  public Integer getPesquisaIdInteger() {
    return this.pesquisaId;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaCnpj' de 'java.lang.String' para 'String'
	 */
  public String getPesquisaCnpj() {
    String str = convert(this.pesquisaCnpj);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaCnpj' de 'String' para 'java.lang.String' 
   */
  public void setPesquisaCnpj(String string) {
    java.lang.String realValue = (java.lang.String) convert(string,java.lang.String.class);
    this.pesquisaCnpj = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaCnpj' como 'String'
	 */
  public String getPesquisaCnpjString() {
    return this.pesquisaCnpj;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaNome' de 'java.lang.String' para 'String'
	 */
  public String getPesquisaNome() {
    String str = convert(this.pesquisaNome);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaNome' de 'String' para 'java.lang.String' 
   */
  public void setPesquisaNome(String string) {
    java.lang.String realValue = (java.lang.String) convert(string,java.lang.String.class);
    this.pesquisaNome = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaNome' como 'String'
	 */
  public String getPesquisaNomeString() {
    return this.pesquisaNome;
  }

	/*GETTER E SETTER DE ATRIBUTOS PARA INVOCAR PESQUISA*/
	/****************************************************/

	/**
	 * Getter que encapsula o Cnpj do ClienteVO encapsulado
	 */
  public String getCnpj() {
  	ClienteVO ClienteVO = getClienteVO();
    String str = null;
		if(ClienteVO!=null){
	    Object object = ClienteVO.getCnpj();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o Cnpj do ClienteVO encapsulado
	 */
  public void setCnpj(String string) {
    java.lang.String realValue = (java.lang.String) convert(string,java.lang.String.class);
    getClienteVO().setCnpj(realValue);
  }

	/**
	 * Getter que encapsula o Id do ClienteVO encapsulado
	 */
  public String getId() {
  	ClienteVO ClienteVO = getClienteVO();
    String str = null;
		if(ClienteVO!=null){
	    Object object = ClienteVO.getId();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Getter que encapsula o Nome do ClienteVO encapsulado
	 */
  public String getNome() {
  	ClienteVO ClienteVO = getClienteVO();
    String str = null;
		if(ClienteVO!=null){
	    Object object = ClienteVO.getNome();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o Nome do ClienteVO encapsulado
	 */
  public void setNome(String string) {
    java.lang.String realValue = (java.lang.String) convert(string,java.lang.String.class);
    getClienteVO().setNome(realValue);
  }

}