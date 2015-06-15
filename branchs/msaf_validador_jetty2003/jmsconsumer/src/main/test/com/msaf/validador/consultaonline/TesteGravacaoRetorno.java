package com.msaf.validador.consultaonline;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.consumer.IValidadorJMSListener;


public class TesteGravacaoRetorno extends TestCase {
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenciaProject");
	static EntityManager em = emf.createEntityManager();


	public static void criaTiposBasicos(){
		
		TpValidVO tipoValidacaoVO = new TpValidVO();
		tipoValidacaoVO.setId(1l);
		tipoValidacaoVO.setDescricao("Sintegra");
		
		ClienteVO clienteVO = new ClienteVO();
		clienteVO.setCnpj("234234234");
		clienteVO.setIdCliente(1l);
		clienteVO.setNome("Gerdau");
		
		PedValidacaoVO pedidoValidacaoVO = new PedValidacaoVO();
		pedidoValidacaoVO.setClienteFk(clienteVO);
		pedidoValidacaoVO.setId(1l);
		
	}
	
	
	public static void main(String[] args) {
		//conexao com BD, 
	
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/consumer/JMSConsumerConfiguration.xml");
		
		//instancia um listener como se fosse  o jmsvalidadorconsumidorBO
		IValidadorJMSListener validadorJMSListener = (IValidadorJMSListener) applicationContext.getBean("messageListener");
		
		
		//recupera um pedido de validação do BD: na realidade isto virá da fila JMS
		PedValidacaoVO pedidoValidacaoVO = em.find(PedValidacaoVO.class, 1l);

		
		//simula que este foi o tipo de validacao solicitado
		TpValidVO tipoValidacaoVO = em.find(TpValidVO.class, 1l);
		
		
		//simula um registro de pessoa lido do excel pelo everton
		PessoaVO registroPessoaVO = new PessoaVO();
		registroPessoaVO.setBairro("ErmelinoMatarazzo");
		registroPessoaVO.setCep("0980877");
		registroPessoaVO.setCnpj("45842622010249");
		registroPessoaVO.setEstado("PR");
		registroPessoaVO.setPedValidFk(pedidoValidacaoVO);
		

		//monta um consultaOnLineDTO para teste
		ParametrosConsultaOnLineDTO consultaOnLineDTO = new ParametrosConsultaOnLineDTO();
		consultaOnLineDTO.setPedidoValidacaoVO(pedidoValidacaoVO);
		consultaOnLineDTO.setRegistroPessoaVO(registroPessoaVO);
		List listTpValidacaoVO = new ArrayList();
		listTpValidacaoVO.add(tipoValidacaoVO);
		consultaOnLineDTO.setTipoValidacaoVO(listTpValidacaoVO);
	
		
		
		
		
		// A partir deste ponto segue o processo normal de processamento de
		// mensagens, como se o parâmetro consultaOnLineDTO tivesse vindo da
		// fila JMS
		//validadorJMSListener.processaConsultaValidador(consultaOnLineDTO);
		
		
	}

}
