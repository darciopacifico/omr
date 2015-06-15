
package com.msaf.validador.consumer;

import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.ParametrosConsultaOnLineDTO;
import com.msaf.validador.consultaonline.ValidadorFacade;
import com.msaf.validador.consultaonline.dao.PedidoValidacaoDAO;
import com.msaf.validador.consultaonline.dao.RegistroPessoaDAO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

/**
 * Implementação do recebedor de mensagens. Esta classe deve conter apenas a
 * orquestração de chamada dos componentes de negócio para efetuar a consulta
 * pela DLL e chamar componente que armazena resultado.
 * 
 * @author dlopes
 * 
 */
//public class ValidadorJMSListener implements MessageListener {
public class PersistenceJMSListener implements MessageListener, MessageDrivenBean {
	
	private Logger log = Logger.getLogger(PersistenceJMSListener.class);
	private static ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/consumer/JMSConsumerConfiguration.xml");

	private PedidoValidacaoDAO pedidoValidacaoDAO;
	private RegistroPessoaDAO registroPessoaDAO;
	
	@Override
	public void onMessage(Message message) {
		try {
			long timeInicial = System.currentTimeMillis();
			if(log.isDebugEnabled()) log.debug("recebendo mensagem");
	
			//recupera parâmetros para consulta: Simplesmente recupera  o ParametrosConsultaOnLineDTO de dentro da mensagem
			Object[] persistObjects;
			try {
				persistObjects = parseMessage(message);
			} catch (ValidadorConsumerException e) {
				if(log.isDebugEnabled()) log.debug("Erro ao tentar receber mensagem JMS!");
				throw new ValidadorRuntimeException("Erro ao tentar receber mensagem JMS!",e);
			}
	
			if(log.isDebugEnabled()) log.debug("Efetuando persistencia...");
			processPersistence(persistObjects);
			if(log.isDebugEnabled()) log.debug("Persistencia efetuada com sucesso.");
	
			if(log.isDebugEnabled()) log.debug("mensagem recebida");
			if(log.isDebugEnabled()) log.debug("Tempo de execucao total da mensagem de persistencia " + Thread.currentThread().hashCode() + ": " + ((System.currentTimeMillis() - timeInicial) / 1000) + " segundos.");
		} catch(Exception e){
			if(log.isDebugEnabled()) log.debug(e);
			this.context.setRollbackOnly();
		}
	}




	/**
	 * @param consultaOnLineDTO
	 */
	public void processPersistence(Object[] persistObjects) {
		//GRAVA CONSULTA
		if(log.isDebugEnabled()) log.debug("Efetuando persistencia do dados...");
		this.gravaResultado((ParametrosConsultaOnLineDTO) persistObjects[0], (List<ResulConsVO>) persistObjects[1]);
		if(log.isDebugEnabled()) log.debug("Persistencia do dados efetuada com sucesso!");
		
		if(log.isDebugEnabled()) log.debug("Recebida mensagem:");
	}

	/**
	 * @param message
	 * @return
	 * @throws ValidadorConsumerException
	 *             Registrar no sistema erro ao tentar consumir mensagem. Não
	 *             deixar subir esta exceção
	 */
	protected Object[] parseMessage(Message message) throws ValidadorConsumerException {
		
		ObjectMessage objectMessage = (ObjectMessage) message;
		Object[] params;
		
		try {
			params = (Object[]) objectMessage.getObject();
		} catch (JMSException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao tentar recuperar o objeto de parametros de consulta!");
			throw new ValidadorConsumerException("Erro ao tentar recuperar o objeto de parametros de consulta!",e);
		}
		return params;
	}

	@Override
	public void ejbRemove() throws EJBException {
		if(log.isDebugEnabled()) log.debug("Removendo instancia de MDB Persistence Listener ...");
		this.pedidoValidacaoDAO = null;
		this.registroPessoaDAO = null;
	}
	
	public void ejbCreate() throws EJBException {
		if(log.isDebugEnabled()) log.debug("Criando nova instancia de MDB Persistence Listener ...");
		this.pedidoValidacaoDAO = (PedidoValidacaoDAO) applicationContext.getBean("pedidoValidacaoDAO");
		this.registroPessoaDAO = (RegistroPessoaDAO) applicationContext.getBean("registroPessoaDAO");
	}

	@Override
	public void setMessageDrivenContext(MessageDrivenContext arg0)
		throws EJBException {
		this.context = arg0;
	}
	
	private MessageDrivenContext context;

	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList) {
		PessoaVO pessoaVO = consultaOnLineDTO.getRegistroPessoaVO();
		
		// apenas para garantir o pedido de validacao
		pessoaVO.setPedValidFk(pesquisaPedValidacaoVO(consultaOnLineDTO));

		for (ResulConsVO resultado : resultadoList) {
			Set<DadosRetInstVO> dadosRetornoList = resultado.getRegistrosRetorno();

			// atribui todos os retornos encontrados ao usuário
			
			pessoaVO.getDadosRetornoFk().addAll(dadosRetornoList);
		}

		try {
			this.incluiRegistroPessoa(pessoaVO); // -- SIMPLES: APENAS INVOCA O EntityManager
		} catch (PersistenciaException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao tentar incluir dados retorno instituicao!");
			throw new EJBException(e);
		}
	}

	/**
	 * @param consultaOnLineDTO
	 * @return
	 * @throws PersistenciaException 
	 */
	protected PedValidacaoVO pesquisaPedValidacaoVO(ParametrosConsultaOnLineDTO consultaOnLineDTO)  {
		if(log.isDebugEnabled()) log.debug("Pesquisa pedido validacao...");
		PedValidacaoVO pedidoValidacao = consultaOnLineDTO.getPedidoValidacaoVO();
			try {
				pedidoValidacao = pedidoValidacaoDAO.buscarPorId(pedidoValidacao.getId());
			} catch (PersistenciaException e) {
				throw new ValidadorRuntimeException("Pedido de validacao nao encontrado! Abortar!",e);
			}
			if(log.isDebugEnabled()) log.debug("Pesquisa pedido validacao executado com sucesso!");
		return pedidoValidacao;
	}
	

	@Transactional
	private void incluiRegistroPessoa(PessoaVO registroPessoaVO)throws PersistenciaException {
		registroPessoaDAO.criarRegistroPessoa(registroPessoaVO);
	}


}
