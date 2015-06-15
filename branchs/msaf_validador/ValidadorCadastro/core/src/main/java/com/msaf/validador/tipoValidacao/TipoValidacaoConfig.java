package com.msaf.validador.tipoValidacao;

import java.util.List;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.tipoValidacao.documento.TipoDocumentoPesquisa;
import com.msaf.validador.tipoValidacao.regra.Regra;

public class TipoValidacaoConfig {

	private TipoValidacao tipoValidacao;
	
	private int ordem;
	
	private Regra regra;
	
	private List<Regra> posPesquisa;
	
	private TipoDocumentoPesquisa tipoDocumento;
	
	public TipoValidacaoConfig(int ordem, TipoValidacao tipoValidacao, Regra regra) {
		this(ordem, regra);
		this.tipoValidacao = tipoValidacao;
	}
	
	public TipoValidacaoConfig(int ordem, Regra regra) {
		this.ordem = ordem;
		this.regra = regra;
	}
	
	public TipoValidacaoConfig(){}

	public TipoValidacao getTipoValidacao() {
		return tipoValidacao;
	}

	public TipoDocumentoPesquisa getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoPesquisa tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public Regra getRegra() {
		return regra;
	}

	public void setRegra(Regra regra) {
		this.regra = regra;
	}

	public List<Regra> getPosPesquisa() {
		return posPesquisa;
	}

	public void setPosPesquisa(List<Regra> posPesquisa) {
		this.posPesquisa = posPesquisa;
	}
	
	public TipoValidacao getTipoValidacao(List<ResulConsVO> list, PessoaVO vo) {

		if(regra == null) {
			return this.tipoValidacao;
		}
		
		regra.setTipoValidacao(tipoValidacao);
		
		if(tipoDocumento != null) {
			tipoDocumento.setPessoa(vo);
			regra.setTipoDocumento(tipoDocumento.getTipoDocumento());
		}
		
		return regra.aplicar(list, vo);
	}

	
	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Orderm: ");
		buffer.append(this.ordem);
		
		buffer.append("| Tipo Validacao: ");
		buffer.append(this.tipoValidacao);
		
		return buffer.toString();
	}
	
	
	public void aplicarPosPesquisa(List<ResulConsVO> list, PessoaVO vo) {
		
		if(this.posPesquisa == null) {
			return;
		}
		
		for(Regra regra: this.posPesquisa) {
			regra.aplicar(list, vo);
		}
	}
}
