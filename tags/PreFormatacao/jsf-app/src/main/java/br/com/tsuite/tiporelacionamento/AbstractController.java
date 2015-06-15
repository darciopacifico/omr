/**
 * 
 */
package br.com.tsuite.tiporelacionamento;

import java.util.ArrayList;
import java.util.List;

import br.com.dlp.framework.pk.AbstractPK;
import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.jazzqa.pessoa.PessoaVO;

/**
 * 
 * @author darcio
 * 
 */
public abstract class AbstractController<B extends AbstractBaseVO<? extends AbstractPK>> {

	public B beanVO = criarNovo();

	public List<B> resultadoConsulta;

	public AbstractController() {
		System.out.println("teste");
	}

	/**
	 * Cria um novo bean
	 * 
	 */
	protected B criarNovo() {
		return null;
	}

	/**
	 * Exibe um registro selecionado
	 * 
	 * @param beanVO
	 * @return
	 */
	public String showDetail(B beanVO) {
		this.beanVO = criarNovo();
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionNovo() {
		this.beanVO = criarNovo();

		return CRUDActionEnum.NOVO;
	}

	/**
	 * @return
	 */
	public CRUDActionEnum actionSalvar() {
		return CRUDActionEnum.SALVAR;
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionAlterar() {
		return CRUDActionEnum.ALTERAR;
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionExcluir() {
		return CRUDActionEnum.EXCLUIR;
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionPesquisar() {
		List<B> modulos = new ArrayList<B>();

		PessoaVO pessoaVO1 = new PessoaVO(1l);
		PessoaVO pessoaVO2 = new PessoaVO(2l);
		PessoaVO pessoaVO3 = new PessoaVO(3l);
		PessoaVO pessoaVO4 = new PessoaVO(4l);

		pessoaVO1.setDescricao("mod 1");
		pessoaVO2.setDescricao("mod 2");
		pessoaVO3.setDescricao("mod 3");
		pessoaVO4.setDescricao("mod 4");

		modulos.add((B) pessoaVO1);
		modulos.add((B) pessoaVO2);
		modulos.add((B) pessoaVO3);
		modulos.add((B) pessoaVO4);

		this.resultadoConsulta = modulos;

		return CRUDActionEnum.PESQUISAR;
	}

	public B getBeanVO() {
		return this.beanVO;
	}

	public void setBeanVO(B beanVO) {
		this.beanVO = beanVO;
	}

	public List<B> getResultadoConsulta() {
		return resultadoConsulta;
	}

	public void setResultadoConsulta(List<B> resultadoConsulta) {
		this.resultadoConsulta = resultadoConsulta;
	}

}
