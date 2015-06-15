package com.msaf.validador.ajax;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.annotations.ScriptScope;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.server.PedidoValidacaoServer;
import com.msaf.validador.integration.hibernate.intf.IPedValidacaoDAO;

@RemoteProxy(scope=ScriptScope.SESSION)
public class PedidoValidacaoAjax {

	private final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/consumer/JMSConsumerConfiguration.xml");

	private PedidoValidacaoServer facade;

	public PedidoValidacaoAjax() {
		facade = (PedidoValidacaoServer) applicationContext.getBean("pedidoValidacaoServer");
	}

	/**
	 * Obtem o percentual de consultas realizadas
	 * 
	 * @param id
	 *            pedido de validação
	 * @return percentual
	 */
	@RemoteMethod
	public Float obterPercentual(Long id) {

		// obtem um dao de pedido validacao
		IPedValidacaoDAO dao = facade.getRepositorio().getPedValidacaoDAO();

		// carrega o pedido de validacao
		PedValidacaoVO pedidoValidacao = dao.findById(id);

		// quantidade de registros do arquivo
		Integer quantidadeRegistros = pedidoValidacao.getQtdeRegistrosArq();

		if (quantidadeRegistros == null || quantidadeRegistros == 0) {
			return 0F;
		}

		// quantidade de pessoas no pedido de validacao
		Integer qtdPessoas = 0;
		
		Float percentual = 0F;
		
		List<PessoaVO> list = facade.getListaPessoaPorPedidoValidacao(pedidoValidacao);
		
		if (list != null) {
			qtdPessoas = list.size();
		}

		percentual = qtdPessoas.floatValue() / quantidadeRegistros.floatValue();

		percentual = percentual * 100;

		if (percentual > 100) {
			percentual = 100f;
		}

		return percentual;
	}

}
