package com.msaf.validador.integration.web.form;

import java.util.Set;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.integration.web.form.base.BaseForm;

public class TesteWebServiceForm extends BaseForm {
	private static final long serialVersionUID = -5022349409330821365L;
	
	
	private PessoaVO pessoaVO = new PessoaVO();
	private ResulConsVO resulConsVO =  new ResulConsVO();
	private String tipo = "";
	private DadosRetInstVO dadosRetInstVO = new DadosRetInstVO();

	public DadosRetInstVO getDadosRetInstVO() {
		return dadosRetInstVO;
	}


	public void setDadosRetInstVO(DadosRetInstVO dadosRetInstVO) {
		this.dadosRetInstVO = dadosRetInstVO;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public ResulConsVO getResulConsVO() {
		return resulConsVO;
	}


	public void setResulConsVO(ResulConsVO resulConsVO) {
		this.resulConsVO = resulConsVO;
	}


	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}


	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}


	public void restart() {
		pessoaVO = new PessoaVO();
		resulConsVO =  new ResulConsVO();
		tipo = "";
		dadosRetInstVO = new DadosRetInstVO();
	}
	
	

}
