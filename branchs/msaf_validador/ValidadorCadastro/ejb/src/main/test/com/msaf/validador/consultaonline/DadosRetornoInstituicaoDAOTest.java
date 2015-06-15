package com.msaf.validador.consultaonline;

//import java.sql.Date;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

import com.msaf.validador.consultaonline.dao.DadosRetornoInstituicaoDAO;
import com.msaf.validador.consultaonline.dao.impl.DadosRetornoInstituicaoDAOImpl;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;

public class DadosRetornoInstituicaoDAOTest extends TestCase {
	
	private Logger log = Logger.getLogger(DadosRetornoInstituicaoDAOTest.class);

	public void testDadosRetornoInstituicaoDAO() {
		fail("Not yet implemented");
	}

	
	private PessoaVO getDadosRegPessoa(){
		PessoaVO dadosRet = new PessoaVO();
		
		dadosRet.setBairro("bairro");
		dadosRet.setCep("79040000");
		dadosRet.setCidade("Coxim");
		dadosRet.setCnpj("9339339344-44");
		dadosRet.setComplemento("apto 15");
		dadosRet.setDataBaixa("10/10/2009");
		dadosRet.setDataConsulta("10/10/2019");
		dadosRet.setDataInclusao("10/10/2039");
		dadosRet.setEnquandramentoEmpresa("enquadramento");
		dadosRet.setEstado("Estado");
		//dadosRet.setIdRegistroPessoa(146);
		dadosRet.setIe("ie");
		dadosRet.setIesEncontradas("1000");
		dadosRet.setLogradouro("rua das carmelias");
		dadosRet.setNumero("234");
		dadosRet.setNumeroConsulta("24");
		dadosRet.setQtdeEncontrada("256");
		dadosRet.setRazaoSocial("razao social");
		dadosRet.setRegimeApuracao("regime apuracao");
		
		//dadosRet.setRegistroPessoaFk(registroPessoaFk);
		
		dadosRet.setSituacao("situacao");
		
		//dadosRet.setTipoResultadoFk(getTipoResultadoVO());
		//dadosRet.setTipoValidacaoFk(getTipoValidacaoVO());
		return dadosRet;	
		}

	
	private DadosRetInstVO getDadosRet(){
	DadosRetInstVO dadosRet = new DadosRetInstVO();
	
	dadosRet.setBairro("bairro");
	dadosRet.setCep("79040000");
	dadosRet.setCidade("Coxim");
	dadosRet.setCnpj("9339339344-44");
	dadosRet.setComplemento("apto 15");
	dadosRet.setDataBaixa("10/10/2009");
	dadosRet.setDataConsulta("10/10/2019");
	dadosRet.setDataInclusao("10/10/2039");
	dadosRet.setEnquandramentoEmpresa("enquadramento");
	dadosRet.setEstado("Estado");
	//dadosRet.setIdDadosRetInst(idDadosRetInst)
	dadosRet.setIe("ie");
	dadosRet.setIesEncontradas("1000");
	dadosRet.setLogradouro("rua das carmelias");
	dadosRet.setNumero("234");
	dadosRet.setNumeroConsulta("24");
	dadosRet.setQtdeEncontrada("256");
	dadosRet.setRazaoSocial("razao social");
	dadosRet.setRegimeApuracao("regime apuracao");
	//dadosRet.setRegistroPessoaFk(getDadosRegPessoa());
	
	dadosRet.setSituacao("situacao");
	
	dadosRet.setTipoResultadoFk(getTipoResultadoVO());
	dadosRet.setTipoValidacaoFk(getTipoValidacaoVO());
	return dadosRet;	
	}
	
	
	private TpResultVO getTipoResultadoVO(){
	TpResultVO tipoResultadoVO = new TpResultVO();
	
		tipoResultadoVO.setDescricao("testando a descricao");
		return tipoResultadoVO;
	}
	
	
	private TpValidVO getTipoValidacaoVO(){
		TpValidVO tipoValidacaoVO = new TpValidVO();
		tipoValidacaoVO.setId(new Long(1));
		tipoValidacaoVO.setDescricao("descricao Tipo validacao");
		
		tipoValidacaoVO.setValidade(new Date());
		
		return tipoValidacaoVO;
		
	}
	
	
	
//	private RegistroPessoaVO getRegistroPessoaVO(){
//
//		RegistroPessoaVO registroP = new RegistroPessoaVO();
//			
//		registroP.setBairro("bairro");
//		registroP.setCep("79040000");
//		registroP.setCidade("Coxim");
//		registroP.setCnpj("9339339344-44");
//		registroP.setComplemento("apto 15");
//		registroP.setDataBaixa(new Date());
//		registroP.setDataConsulta(new Date());
//		registroP.setDataInclusao(new Date());
//		registroP.setEnquandramentoEmpresa("enquadramento");
//		registroP.setEstado("Estado");
//			//registroP.setIdDadosRetInst(idDadosRetInst)
//		registroP.setIe("ie");
//		registroP.setIesEncontradas(new BigDecimal(1000));
//		registroP.setLogradouro("rua das carmelias");
//		registroP.setNumero("234");
//		registroP.setNumeroConsulta(new BigDecimal(24));
//		registroP.setQtdeEncontrada(new BigDecimal(345));
//		registroP.setRazaoSocial("razao social");
//		registroP.setRegimeApuracao("regime apuracao");
//			//registroP.setRegistroPessoaFk(registroPessoaFk);
//		registroP.setSituacao("situacao");
//			//registroP.setTipoResultadoFk(tipoResultadoFk);
//			//registroP.setTipoValidacaoFk(tipoValidacaoFk);
//			
//			return registroP;	
//			}
//			
		
		
	public void testCriarDadosRetornoInstituicaoDAO() {
		DadosRetornoInstituicaoDAO dadosRetDAO = new DadosRetornoInstituicaoDAOImpl();
		try {
			dadosRetDAO.criarDadosRetornoInstituicao(getDadosRet());
			if(log.isDebugEnabled()) log.debug("inseriu...");
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}

	
	public void testBuscarDadosRetornoId() {
		DadosRetornoInstituicaoDAO dadosRetDAO = new DadosRetornoInstituicaoDAOImpl();
		DadosRetInstVO ret;
		try {
			ret = dadosRetDAO.buscarDadosRetornoId(new Long(1));
			if(log.isDebugEnabled()) log.debug(ret.getCep());
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}

	
	public void testAtualizarDadosRetornoInstituicao() {

		DadosRetornoInstituicaoDAO dadosRetDAO = new DadosRetornoInstituicaoDAOImpl();
		DadosRetInstVO ret;
		try {
			ret = dadosRetDAO.buscarDadosRetornoId(new Long(1));
			ret.setDataConsulta("03/03/2009");
			dadosRetDAO.atualizarDadosRetornoInstituicao(ret);

		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fail("Atualizado");
	}

	public void testRemoverDadosRetornoInstituicao() {
		DadosRetornoInstituicaoDAO dadosRetDAO = new DadosRetornoInstituicaoDAOImpl();
		DadosRetInstVO ret;
		try {
			ret = dadosRetDAO.buscarDadosRetornoId(new Long(1));
			dadosRetDAO.removerDadosRetornoInstituicao(ret);
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		fail("Not yet implemented");
	}

	public void testBuscarTodos() {
		DadosRetornoInstituicaoDAO dadosRetDAO = new DadosRetornoInstituicaoDAOImpl();
		List<DadosRetInstVO> lista;
		try {
			lista = dadosRetDAO.buscarTodos();
			if(log.isDebugEnabled()) log.debug(":"+lista.size());
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	public void testBuscarPorNome() {
		fail("Not yet implemented");
	}

	
	public void testClose() {
		fail("Not yet implemented");
	}

}
