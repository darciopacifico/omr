package com.msaf.validador.tipoValidacao.regra.impl;

import java.util.List;
import java.util.Set;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

public class RegraPosPesquisaReceitaFederal extends AbstractRegra {

	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO pessoa) {
		
		this.populaPessoaVOComDadosRetornoRF(resultadosAnteriores, pessoa);

		return null;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		
	}

	
	private void populaPessoaVOComDadosRetornoRF(List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		
		if((resultadosConsultas == null) || (pessoaVO == null)) {
			return;
		}
		
		for (ResulConsVO resultadoConsulta : resultadosConsultas) {
			
			Set<DadosRetInstVO> dadosRetInstList = resultadoConsulta.getRegistrosRetorno();

			for (DadosRetInstVO dadosRetornoInstituicaoVO : dadosRetInstList) {

				pessoaVO.setEstado(dadosRetornoInstituicaoVO.getEstado());

				// validacao para nao sobrescrever as informacoes originais fornecidas pelo usuário em caso
				if (dadosRetornoInstituicaoVO.getCnpj() != null){
					pessoaVO.setCnpj(liparCaracter(dadosRetornoInstituicaoVO.getCnpj()));
				}

				if (dadosRetornoInstituicaoVO.getIe() != null){
					pessoaVO.setIe(liparCaracter(dadosRetornoInstituicaoVO.getIe()));
				}

				if (dadosRetornoInstituicaoVO.getCpf() != null){
					pessoaVO.setCpf(liparCaracter(dadosRetornoInstituicaoVO.getCpf()));
				}
			}
		}
		
	}

	private String liparCaracter(String valor) {
		valor = valor.replaceAll("[ -._/]", "");
		return valor;
	}
	
	
}
