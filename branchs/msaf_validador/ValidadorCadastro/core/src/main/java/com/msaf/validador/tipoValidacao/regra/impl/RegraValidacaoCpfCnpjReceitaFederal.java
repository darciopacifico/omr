package com.msaf.validador.tipoValidacao.regra.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.msaf.validador.consultaonline.ErrorPesquisa;
import com.msaf.validador.consultaonline.TipoDocumento;
import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.tipoValidacao.regra.AbstractRegra;

public class RegraValidacaoCpfCnpjReceitaFederal extends AbstractRegra {

	private TipoValidacao tipoValidacao;
	
	@Override
	public TipoValidacao aplicar(List<ResulConsVO> resultadosAnteriores, PessoaVO vo) {
		
		ResulConsVO result = validarCnpj(vo);
		
		if(result != null){
			resultadosAnteriores.add(result);
			this.interruptPesquisa();
		}
		
		return tipoValidacao;
	}

	@Override
	public void setTipoValidacao(TipoValidacao tipoValidacao) {
		this.tipoValidacao = tipoValidacao;
	}

	
	
	private ResulConsVO validarCnpj(PessoaVO pessoa) {

		boolean isValid = true;
		
		if(super.getTipoDocumento().equals(TipoDocumento.RECEITA_CNPJ)) {
			if(!Util.isCnpj(pessoa.getCnpj())) {
				isValid = false;
			}
		}else if(super.getTipoDocumento().equals(TipoDocumento.RECEITA_CPF)) {
			if(!Util.isCpf(pessoa.getCpf())) {
				isValid = false;
			}
		}

		// se não for valido
		if(isValid) {
			return null;
		}
			
		ResulConsVO vo = new ResulConsVO();
		
		vo.setCodErro("" + ErrorPesquisa.DIGITOR_VERIFICADOR_INVALIDO.getCodigo());
		vo.setErro(ErrorPesquisa.DIGITOR_VERIFICADOR_INVALIDO.getDescricao());
		vo.setQuantidade(1);
		
		DadosRetInstVO dadosVO = new DadosRetInstVO();
		
		TpResultVO tipoResultadoFk = new TpResultVO();
		
		tipoResultadoFk.setId(ErrorPesquisa.DIGITOR_VERIFICADOR_INVALIDO.getCodigo());
		tipoResultadoFk.setDescricao(ErrorPesquisa.DIGITOR_VERIFICADOR_INVALIDO.getDescricao());
		
		Set<DadosRetInstVO> dadosRetornoInstituicaoVOs = new HashSet<DadosRetInstVO>();
		
		dadosRetornoInstituicaoVOs.add(dadosVO);
		
		vo.setRegistrosRetorno(dadosRetornoInstituicaoVOs);
		
		dadosVO.setTipoResultadoFk(tipoResultadoFk);
		
		return vo;
			
	}
}
