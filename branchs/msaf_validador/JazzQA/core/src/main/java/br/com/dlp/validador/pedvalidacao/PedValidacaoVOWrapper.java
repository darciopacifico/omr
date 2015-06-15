package br.com.dlp.validador.pedvalidacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.validador.cliente.ClienteVO;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementacao de IVOWrapper para PedValidacaoVO
 **/
public class PedValidacaoVOWrapper extends br.com.dlp.framework.vowrapper.AbstractVOWrapper{
	/**
	 * Controle de versão para classes serializáveis
	 */
	private static final long serialVersionUID = 907976127239847L;

	/*mecanismo de indexOf para Encapsular o atributo PedValidacaoVO.getClienteVO() */
	private List listClienteVOs = new ArrayList();

	/*********************************/
	/*ATRIBUTOS PARA INVOCAR PESQUISA*/

	private java.lang.Integer pesquisaId;

	private java.util.Date pesquisaDataDownloadDe;
	private java.util.Date pesquisaDataDownloadAte;

	private java.util.Date pesquisaDataSolicitacaoDe;
	private java.util.Date pesquisaDataSolicitacaoAte;

	private java.util.Date pesquisaDataTerminoDe;
	private java.util.Date pesquisaDataTerminoAte;

	private java.lang.Integer pesquisaQtdeRegistrosArqDe;
	private java.lang.Integer pesquisaQtdeRegistrosArqAte;

	private java.lang.Integer pesquisaQtdeRegistrosProcDe;
	private java.lang.Integer pesquisaQtdeRegistrosProcAte;

	/*POR PADRAO, O ITEM SELECIONADO INICIALMENTE É 'Todos' (-1 é o value do html:radio 'todos')*/
	private int pesquisaCliente=-1;

	/*ATRIBUTOS PARA INVOCAR PESQUISA*/
	/*********************************/

	/**
	 * Construtor de 'PedValidacaoVOWrapper' que recebe o 
	 * ResourceBundle contendo o padrão de formatação de números e datas internacionalizado
	 */
	public PedValidacaoVOWrapper(ResourceBundle resourceBundle){
		super(resourceBundle);
	}

	/**
	 * Recupera o 'PedValidacaoVO' encapsulado
	 */
	public PedValidacaoVO getPedValidacaoVO(){
		return (PedValidacaoVO)getBaseVO();
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
	 * Converte e RETORNA o atributo 'pesquisaDataDownloadDe' de 'java.util.Date' para 'String'
	 */
  public String getPesquisaDataDownloadDe() {
    String str = convert(this.pesquisaDataDownloadDe);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaDataDownloadDe' de 'String' para 'java.util.Date' 
   */
  public void setPesquisaDataDownloadDe(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    this.pesquisaDataDownloadDe = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaDataDownloadDe' como 'Date'
	 */
  public Date getPesquisaDataDownloadDeDate() {
    return this.pesquisaDataDownloadDe;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaDataDownloadAte' de 'java.util.Date' para 'String'
	 */
  public String getPesquisaDataDownloadAte() {
    String str = convert(this.pesquisaDataDownloadAte);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaDataDownloadAte' de 'String' para 'java.util.Date' 
   */
  public void setPesquisaDataDownloadAte(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    this.pesquisaDataDownloadAte = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaDataDownloadAte' como 'Date'
	 */
  public Date getPesquisaDataDownloadAteDate() {
    return this.pesquisaDataDownloadAte;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaDataSolicitacaoDe' de 'java.util.Date' para 'String'
	 */
  public String getPesquisaDataSolicitacaoDe() {
    String str = convert(this.pesquisaDataSolicitacaoDe);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaDataSolicitacaoDe' de 'String' para 'java.util.Date' 
   */
  public void setPesquisaDataSolicitacaoDe(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    this.pesquisaDataSolicitacaoDe = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaDataSolicitacaoDe' como 'Date'
	 */
  public Date getPesquisaDataSolicitacaoDeDate() {
    return this.pesquisaDataSolicitacaoDe;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaDataSolicitacaoAte' de 'java.util.Date' para 'String'
	 */
  public String getPesquisaDataSolicitacaoAte() {
    String str = convert(this.pesquisaDataSolicitacaoAte);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaDataSolicitacaoAte' de 'String' para 'java.util.Date' 
   */
  public void setPesquisaDataSolicitacaoAte(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    this.pesquisaDataSolicitacaoAte = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaDataSolicitacaoAte' como 'Date'
	 */
  public Date getPesquisaDataSolicitacaoAteDate() {
    return this.pesquisaDataSolicitacaoAte;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaDataTerminoDe' de 'java.util.Date' para 'String'
	 */
  public String getPesquisaDataTerminoDe() {
    String str = convert(this.pesquisaDataTerminoDe);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaDataTerminoDe' de 'String' para 'java.util.Date' 
   */
  public void setPesquisaDataTerminoDe(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    this.pesquisaDataTerminoDe = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaDataTerminoDe' como 'Date'
	 */
  public Date getPesquisaDataTerminoDeDate() {
    return this.pesquisaDataTerminoDe;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaDataTerminoAte' de 'java.util.Date' para 'String'
	 */
  public String getPesquisaDataTerminoAte() {
    String str = convert(this.pesquisaDataTerminoAte);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaDataTerminoAte' de 'String' para 'java.util.Date' 
   */
  public void setPesquisaDataTerminoAte(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    this.pesquisaDataTerminoAte = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaDataTerminoAte' como 'Date'
	 */
  public Date getPesquisaDataTerminoAteDate() {
    return this.pesquisaDataTerminoAte;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaQtdeRegistrosArqDe' de 'java.lang.Integer' para 'String'
	 */
  public String getPesquisaQtdeRegistrosArqDe() {
    String str = convert(this.pesquisaQtdeRegistrosArqDe);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaQtdeRegistrosArqDe' de 'String' para 'java.lang.Integer' 
   */
  public void setPesquisaQtdeRegistrosArqDe(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    this.pesquisaQtdeRegistrosArqDe = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaQtdeRegistrosArqDe' como 'Integer'
	 */
  public Integer getPesquisaQtdeRegistrosArqDeInteger() {
    return this.pesquisaQtdeRegistrosArqDe;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaQtdeRegistrosArqAte' de 'java.lang.Integer' para 'String'
	 */
  public String getPesquisaQtdeRegistrosArqAte() {
    String str = convert(this.pesquisaQtdeRegistrosArqAte);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaQtdeRegistrosArqAte' de 'String' para 'java.lang.Integer' 
   */
  public void setPesquisaQtdeRegistrosArqAte(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    this.pesquisaQtdeRegistrosArqAte = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaQtdeRegistrosArqAte' como 'Integer'
	 */
  public Integer getPesquisaQtdeRegistrosArqAteInteger() {
    return this.pesquisaQtdeRegistrosArqAte;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaQtdeRegistrosProcDe' de 'java.lang.Integer' para 'String'
	 */
  public String getPesquisaQtdeRegistrosProcDe() {
    String str = convert(this.pesquisaQtdeRegistrosProcDe);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaQtdeRegistrosProcDe' de 'String' para 'java.lang.Integer' 
   */
  public void setPesquisaQtdeRegistrosProcDe(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    this.pesquisaQtdeRegistrosProcDe = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaQtdeRegistrosProcDe' como 'Integer'
	 */
  public Integer getPesquisaQtdeRegistrosProcDeInteger() {
    return this.pesquisaQtdeRegistrosProcDe;
  }

	/**
	 * Converte e RETORNA o atributo 'pesquisaQtdeRegistrosProcAte' de 'java.lang.Integer' para 'String'
	 */
  public String getPesquisaQtdeRegistrosProcAte() {
    String str = convert(this.pesquisaQtdeRegistrosProcAte);
    return str;
  }
  /**
   * Converte e ATRIBUI o parâmetro 'pesquisaQtdeRegistrosProcAte' de 'String' para 'java.lang.Integer' 
   */
  public void setPesquisaQtdeRegistrosProcAte(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    this.pesquisaQtdeRegistrosProcAte = realValue;
  }
	/**
	 * Retorna o valor do atributo encapsulado para pesquisa 'pesquisaQtdeRegistrosProcAte' como 'Integer'
	 */
  public Integer getPesquisaQtdeRegistrosProcAteInteger() {
    return this.pesquisaQtdeRegistrosProcAte;
  }

	/**
	 * Retorna o índice do VO selecionado 'pesquisaCliente' da lista 'listClienteVOs' 
	 */
  public int getPesquisaCliente() {
	  return this.pesquisaCliente;
  }
  /**
   * Atribui o índice do VO selecionado da lista 'listClienteVOs' 
   */
  public void setPesquisaCliente(int index) {
  	this.pesquisaCliente=index;
  }
  /**
   * Recupera e retorna um IPK da lista 'listClienteVOs' de acordo 
   * com o índice armazenado em 'this.pesquisaCliente'
   */
  public IPK getPesquisaClienteIPK() {
  	int index = getPesquisaCliente();
  	List list = getListClienteVOs();
  	if(list==null || list.size()==0 || index<0 || index >list.size() ){
  	  return null;
  	}
  	ICadastroBaseVO cadastroBaseVO = (ICadastroBaseVO)list.get(index);

  	IPK ipk = null;
  	if(cadastroBaseVO!=null){
	  	ipk = cadastroBaseVO.getIPK();
  	}
  	return ipk;
  }

	/*GETTER E SETTER DE ATRIBUTOS PARA INVOCAR PESQUISA*/
	/****************************************************/

				/*mecanismos de relacionamento com a entidade ClienteVO*/

	/**
	 * Retorna a lista indexada do atributo 'ClienteVO'
	 */
	public List getListClienteVOs(){
		return this.listClienteVOs;
	}

	/**
	 * Atribui a lista indexada do atributo 'ClienteVO'
	 */
	public void setListClienteVOs(List list){
		this.listClienteVOs=list;
	}
	/**
	 * Retorna o índice da lista de acordo com o VO contido em 'PedValidacaoVO.getClienteVO()'
   */
  public int getClienteVO() {
    IBaseVO baseVO = getPedValidacaoVO().getClienteVO();
    List list = getListClienteVOs();
    int indexOf = getIndexOf(list,baseVO);
    return indexOf;
  }

	/**
	 * Recupera um VO da lista 'listClienteVOs' de acordo com o índice 'indexOf' e atribui no 'PedValidacaoVO' encapsulado
   */
  public void setClienteVO(int indexOf) {
    List list = getListClienteVOs();
    ClienteVO baseVO = null;
    /*se indexOf for igual a -1, é pq o usuário não selecionou nenhum item no combo*/
	if(indexOf!=-1){
	    baseVO = (ClienteVO) list.get(indexOf);
    }
    PedValidacaoVO pedValidacaoVO = getPedValidacaoVO();
    pedValidacaoVO.setClienteVO(baseVO);
  }

	/**
	 * Getter que encapsula o DataDownload do PedValidacaoVO encapsulado
	 */
  public String getDataDownload() {
  	PedValidacaoVO PedValidacaoVO = getPedValidacaoVO();
    String str = null;
		if(PedValidacaoVO!=null){
	    Object object = PedValidacaoVO.getDataDownload();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o DataDownload do PedValidacaoVO encapsulado
	 */
  public void setDataDownload(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    getPedValidacaoVO().setDataDownload(realValue);
  }

	/**
	 * Getter que encapsula o DataSolicitacao do PedValidacaoVO encapsulado
	 */
  public String getDataSolicitacao() {
  	PedValidacaoVO PedValidacaoVO = getPedValidacaoVO();
    String str = null;
		if(PedValidacaoVO!=null){
	    Object object = PedValidacaoVO.getDataSolicitacao();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o DataSolicitacao do PedValidacaoVO encapsulado
	 */
  public void setDataSolicitacao(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    getPedValidacaoVO().setDataSolicitacao(realValue);
  }

	/**
	 * Getter que encapsula o DataTermino do PedValidacaoVO encapsulado
	 */
  public String getDataTermino() {
  	PedValidacaoVO PedValidacaoVO = getPedValidacaoVO();
    String str = null;
		if(PedValidacaoVO!=null){
	    Object object = PedValidacaoVO.getDataTermino();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o DataTermino do PedValidacaoVO encapsulado
	 */
  public void setDataTermino(String string) {
    java.util.Date realValue = (java.util.Date) convert(string,java.util.Date.class);
    getPedValidacaoVO().setDataTermino(realValue);
  }

	/**
	 * Getter que encapsula o Id do PedValidacaoVO encapsulado
	 */
  public String getId() {
  	PedValidacaoVO PedValidacaoVO = getPedValidacaoVO();
    String str = null;
		if(PedValidacaoVO!=null){
	    Object object = PedValidacaoVO.getId();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Getter que encapsula o QtdeRegistrosArq do PedValidacaoVO encapsulado
	 */
  public String getQtdeRegistrosArq() {
  	PedValidacaoVO PedValidacaoVO = getPedValidacaoVO();
    String str = null;
		if(PedValidacaoVO!=null){
	    Object object = PedValidacaoVO.getQtdeRegistrosArq();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o QtdeRegistrosArq do PedValidacaoVO encapsulado
	 */
  public void setQtdeRegistrosArq(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    getPedValidacaoVO().setQtdeRegistrosArq(realValue);
  }

	/**
	 * Getter que encapsula o QtdeRegistrosProc do PedValidacaoVO encapsulado
	 */
  public String getQtdeRegistrosProc() {
  	PedValidacaoVO PedValidacaoVO = getPedValidacaoVO();
    String str = null;
		if(PedValidacaoVO!=null){
	    Object object = PedValidacaoVO.getQtdeRegistrosProc();
			str = convert(object);
		}
    return str;
  }

	/**
	 * Setter que encapsula o QtdeRegistrosProc do PedValidacaoVO encapsulado
	 */
  public void setQtdeRegistrosProc(String string) {
    java.lang.Integer realValue = (java.lang.Integer) convert(string,java.lang.Integer.class);
    getPedValidacaoVO().setQtdeRegistrosProc(realValue);
  }

}