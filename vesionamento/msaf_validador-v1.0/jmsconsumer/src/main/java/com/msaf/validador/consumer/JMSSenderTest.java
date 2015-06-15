package com.msaf.validador.consumer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.ParametrosConsultaOnLineDTO;
import com.msaf.validador.consultaonline.ValidadorFacadeMQImpl;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;


public class JMSSenderTest {
	Logger log = Logger.getLogger(JMSSenderTest.class);

	//FIXME: ESTES SERVICOS DEVEM SER INSERIDOS NO BD.
	private static final int SINTEGRA = 1;
	private static final int RECEITA_FEDERAL = 3; 
	private static final int SUFRAMA = 5; 
	private static final int PREF_SP = 7; // em implementacao
	private static final int CRM = 8;
	private static final int ANVISA = 9; 
	private static final int MUNICIPIOS_IBGE = 10; 
	private static final int ANP = 13;
	
	
	private static PessoaVO getPessoaVO(){
		PessoaVO pessoaVO = new PessoaVO();
		
		pessoaVO.setBairro("bairro");
		pessoaVO.setCep("79040000");
						 
		// sintegra
		//pessoaVO.setCnpj("45842622010249");
		//sumafra
		//pessoaVO.setCnpj("63592448000242");
		//crm
		//pessoaVO.setCnpj("123454");
		//ibge
		pessoaVO.setCnpj("");
		//anp
		pessoaVO.setCnpj("03222638000165");
		
		//ibge
		pessoaVO.setCidade("Sao Paulo");
		pessoaVO.setComplemento("apto 15");
		pessoaVO.setDataBaixa("10/10/2009");
		pessoaVO.setDataConsulta("10/10/2019");
		pessoaVO.setDataInclusao("10/10/2039");
		pessoaVO.setEnquandramentoEmpresa("Enquadramento");
		//pessoaVO.setEstado("PR");
		//crm
		pessoaVO.setEstado("SP");
		pessoaVO.setIe("ie");
		pessoaVO.setIesEncontradas("1000");
		pessoaVO.setLogradouro("rua das carmelias");
		pessoaVO.setNumero("234");
		pessoaVO.setNumeroConsulta("24");
		pessoaVO.setQtdeEncontrada("256");
		pessoaVO.setRazaoSocial("razao social");
		pessoaVO.setRegimeApuracao("regime apuracao");
		pessoaVO.setSituacao("situacao");
		
		
		//dadosRet.setIdRegistroPessoa(idRegistroPessoa);
		return pessoaVO;	
		}
		
	
	private static PedValidacaoVO getPedValidacaoVO(){
		PedValidacaoVO pedidoValidacao = new PedValidacaoVO();
		 pedidoValidacao.setId(132);
			return pedidoValidacao;
		}	
	
	
		private static TpResultVO getTipoResultadoVO(){
		TpResultVO tipoResultadoVO = new TpResultVO();
		    tipoResultadoVO.setId(new Long(1));
			tipoResultadoVO.setDescricao("testando a descricao");
			return tipoResultadoVO;
		}
		
		
		
		/**
		 * Retorna os tipos de validacoes.
		 * @return
		 */
		private static List <TpValidVO> getTipoValidacaoVO(){
			
			List<TpValidVO> tipoValidacaoList = new ArrayList<TpValidVO>();
//			tipoValidacaoList.add(getTipoValidacaoANP()); 
//			tipoValidacaoList.add(getTipoValidacaoMUNICIPIOS_IBGE());
//			tipoValidacaoList.add(getTipoValidacaoCRM());
//			tipoValidacaoList.add(getTipoValidacaoSumafra());
			tipoValidacaoList.add(getTipoValidacaoSintegra());

			
			return tipoValidacaoList;
			
		}
		

		private static TpValidVO getTipoValidacaoSintegra(){
			TpValidVO tipoValidacaoVO = new TpValidVO();
			tipoValidacaoVO.setId(new Long(SINTEGRA));
			tipoValidacaoVO.setDescricao(" validacao SINTEGRA");
			tipoValidacaoVO.setValidade(new Date());
			tipoValidacaoVO.setValidade(new Date());
			 
			return tipoValidacaoVO;
		}

		
		//FIXME: FALTA MONTAR A ESTRUTURA DE CONSULTA PARA A RF
//		private static TpValidVO getTipoValidacaoReceitaFederal(){
//			TpValidVO tipoValidacaoVO = new TpValidVO();
//			tipoValidacaoVO.setId(new Long(RECEITA_FEDERAL));
//			tipoValidacaoVO.setDescricao(" validacao RECEITA_FEDERAL");
//			tipoValidacaoVO.setValidade(new Date());
//			 
//			return tipoValidacaoVO;
//		}
		
		
		private static TpValidVO getTipoValidacaoSumafra(){
			TpValidVO tipoValidacaoVO = new TpValidVO();
			tipoValidacaoVO.setId(new Long(SUFRAMA));
			tipoValidacaoVO.setDescricao(" validacao SUFRAMA");
			tipoValidacaoVO.setValidade(new Date());
			 
			return tipoValidacaoVO;
		}
		

		private static TpValidVO getTipoValidacaoCRM(){
			TpValidVO tipoValidacaoVO = new TpValidVO();
			tipoValidacaoVO.setId(new Long(CRM));
			tipoValidacaoVO.setDescricao(" validacao CRM");
			tipoValidacaoVO.setValidade(new Date());
			 
			return tipoValidacaoVO;
		}
		
		
		private static TpValidVO getTipoValidacaoMUNICIPIOS_IBGE(){
			TpValidVO tipoValidacaoVO = new TpValidVO();
			tipoValidacaoVO.setId(new Long(MUNICIPIOS_IBGE));
			tipoValidacaoVO.setDescricao(" validacao MUNICIPIOS_IBGE");
			tipoValidacaoVO.setValidade(new Date());
			 
			return tipoValidacaoVO;
		}
		
		
		private static TpValidVO getTipoValidacaoANP(){
			TpValidVO tipoValidacaoVO = new TpValidVO();
			tipoValidacaoVO.setId(new Long(ANP));
			tipoValidacaoVO.setDescricao(" validacao ANP");
			tipoValidacaoVO.setValidade(new Date());
			 
			return tipoValidacaoVO;
		}
		

//		123454
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ValidadorFacadeMQImpl facadeMQImpl = new ValidadorFacadeMQImpl();
		
		ParametrosConsultaOnLineDTO parameterObject = new ParametrosConsultaOnLineDTO();
		
		parameterObject.setRegistroPessoaVO(getPessoaVO());
		parameterObject.setTipoValidacaoVO(getTipoValidacaoVO());
		
		parameterObject.setPedidoValidacaoVO(getPedValidacaoVO());
		
		
		for (int i = 0; i < 1; i++) {
			 facadeMQImpl = new ValidadorFacadeMQImpl();
		}

	}

}
