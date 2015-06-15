package com.msaf.validador.consultaonline;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.msaf.validador.consultaonline.dao.PedidoValidacaoDAO;
import com.msaf.validador.consultaonline.dao.RegistroPessoaDAO;
import com.msaf.validador.consultaonline.dao.impl.PedidoValidacaoDAOImpl;
import com.msaf.validador.consultaonline.dao.impl.RegistroPessoaDAOImpl;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;

public class RegistroPessoaDAOImplTest {

	
	
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
		//dadosRet.setIdRegistroPessoa(149);
		dadosRet.setIe("ie");
		dadosRet.setIesEncontradas("1000");
		dadosRet.setLogradouro("rua das carmelias");
		dadosRet.setNumero("234");
		dadosRet.setNumeroConsulta("24");
		dadosRet.setQtdeEncontrada("256");
		dadosRet.setRazaoSocial("razao social");
		dadosRet.setRegimeApuracao("regime apuracao");
		
		
		dadosRet.setPedValidFk(getPedValidacaoVO(dadosRet));
		
		//dadosRet.setRegistroPessoaFk(registroPessoaFk);
		
		dadosRet.setSituacao("situacao");
		
		Set<DadosRetInstVO> dadosRetornoList = new HashSet<DadosRetInstVO>() ;
		
		dadosRetornoList.add(getDadosRet());
		dadosRetornoList.add(getDadosRet());
		dadosRet.setDadosRetornoFk(dadosRetornoList);
		
		//dadosRet.setTipoResultadoFk(getTipoResultadoVO());
		//dadosRet.setTipoValidacaoFk(getTipoValidacaoVO());
		return dadosRet;	
		}

	private PedValidacaoVO getPedValidacaoVO( PessoaVO dadosRet){
		PedidoValidacaoDAO pedidoValidacaoDAO  = new PedidoValidacaoDAOImpl();
		
		try {
			PedValidacaoVO pedidoValidacaoVO = pedidoValidacaoDAO.buscarPorId(new Long(123));
			pedidoValidacaoVO.getPessoaFK().add(dadosRet);
			return pedidoValidacaoVO;
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	//dadosRet.setRegistroPessoaFk(registroPessoaFk);
	
	dadosRet.setSituacao("situacao");
	
	dadosRet.setTipoResultadoFk(getTipoResultadoVO());
	//dadosRet.setTipoValidacaoFk(getTipoValidacaoVO());
	//dadosRet.setRegistroPessoaFk(getDadosRegPessoa());
	return dadosRet;	
	}
	
	
	private TpResultVO getTipoResultadoVO(){
		TpResultVO tipoResultadoVO = new TpResultVO();
		
			tipoResultadoVO.setId(new Long(1));
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
		
		
	@Test
	public void testCriarRegistroPessoa() {
		RegistroPessoaDAO regPessoa = new RegistroPessoaDAOImpl();
		
		PessoaVO registroPessoaVO = this.getDadosRegPessoa(); 
		
		
		regPessoa.criarRegistroPessoa(registroPessoaVO);
		System.out.println("Criacao de registro...");
		fail("Not yet implemented");
	}

	@Test
	public void testBuscarDadosRetornoId() {
		fail("Not yet implemented");
	}

	@Test
	public void testAtualizarRegistroPessoa() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoverRegistroPessoa() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuscarTodos() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuscarPorNome() {
		fail("Not yet implemented");
	}

}
