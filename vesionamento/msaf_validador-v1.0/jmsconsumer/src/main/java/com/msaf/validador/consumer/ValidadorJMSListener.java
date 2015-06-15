package com.msaf.validador.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.ParametrosConsultaOnLineDTO;
import com.msaf.validador.consultaonline.ValidadorFacade;
import com.msaf.validador.consultaonline.exceptions.BaseValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;

/**
 * Implementação do recebedor de mensagens. Esta classe deve conter apenas a
 * orquestração de chamada dos componentes de negócio para efetuar a consulta
 * pela DLL e chamar componente que armazena resultado.
 * 
 * @author dlopes
 * 
 */
//public class ValidadorJMSListener implements MessageListener {
public class ValidadorJMSListener implements IValidadorJMSListener {
	private Logger log = Logger.getLogger(ValidadorJMSListener.class);
	private static long PROCESSAMENTO_SUCESSO;

	private ValidadorFacade validadorFacade;
	SwingConsumerMonitor swingConsumerMonitor;
	
	private DllDadosDTO configuracaoDll;
	
	@Override
	public void onMessage(Message message) {
		log.debug("recebendo mensagem");

		swingConsumerMonitor.registraEventoNaConsole("Recebendo mensagem...");
		
		//recupera parâmetros para consulta: Simplesmente recupera  o ParametrosConsultaOnLineDTO de dentro da mensagem
		ParametrosConsultaOnLineDTO consultaOnLineDTO;
		try {
			consultaOnLineDTO = recuperaParametroConsulta(message);
		} catch (ValidadorConsumerException e) {
			validadorFacade.registraErroDeProcessamento(e);
			swingConsumerMonitor.registraEventoNaConsole("Erro ao tentar receber mensagem JMS!");
			throw new ValidadorRuntimeException("Erro ao tentar receber mensagem JMS!",e);
		}

		swingConsumerMonitor.registraEventoNaConsole("Efetuando validações...");
		processaConsultaValidador(consultaOnLineDTO, this.configuracaoDll);
		swingConsumerMonitor.registraEventoNaConsole("Validações contidas na mensagem foram efetuadas.");

	}




	/**
	 * @param consultaOnLineDTO
	 */
	public void processaConsultaValidador(ParametrosConsultaOnLineDTO consultaOnLineDTO, DllDadosDTO configuracaoDLL) {
		//Executa consulta dos dados do cliente: fazer funcionar

		List<ResulConsVO> resultadosConsultas = new ArrayList<ResulConsVO>();
		
		try {
			validadorFacade.consultaDadosCliente(consultaOnLineDTO, configuracaoDLL, resultadosConsultas);
		} catch (BaseValidadorException e) {
			log.error("Erro consultando dados de cliente!",e);
			swingConsumerMonitor.registraEventoNaConsole("Erro consultando dados do cliente!");
			validadorFacade.registraErroDeProcessamento(e, consultaOnLineDTO);
			return;
		}
		
		
		//GRAVA CONSULTA
		validadorFacade.gravaResultado(consultaOnLineDTO, resultadosConsultas);
		
		log.debug("Recebida mensagem:");
	}




	/**
	 * 
	 * @param consultaOnLineDTO
	 * @param resultado
	 * @param msgResultado
	 */
	private void associaPedidoDeValidacaoE_Resultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultado) {
		
		TpResultVO tpResSucesso = getTpResulSucesso();
		
		PessoaVO pessoaVO = consultaOnLineDTO.getRegistroPessoaVO();
		
		for (ResulConsVO resulConsVO : resultado) {
			
			Set<DadosRetInstVO> dadosRetInst = resulConsVO.getRegistrosRetorno();
			for (DadosRetInstVO dadosRetInstVO : dadosRetInst) {
				dadosRetInstVO.setTipoResultadoFk(tpResSucesso);
			}
		}
	}




	/**
	 * @return
	 */
	protected TpResultVO getTpResulSucesso() {
		TpResultVO tpResSucesso = new TpResultVO();
		tpResSucesso.setDescricao("Documento processado com sucesso.");
		tpResSucesso.setId(1l);
		return tpResSucesso;
	}




	/**
	 * @param message
	 * @return
	 * @throws ValidadorConsumerException
	 *             Registrar no sistema erro ao tentar consumir mensagem. Não
	 *             deixar subir esta exceção
	 */
	protected ParametrosConsultaOnLineDTO recuperaParametroConsulta(Message message) throws ValidadorConsumerException {
		
		ObjectMessage objectMessage = (ObjectMessage) message;
		ParametrosConsultaOnLineDTO consultaOnLineDTO;
		
		try {
			consultaOnLineDTO = (ParametrosConsultaOnLineDTO) objectMessage.getObject();
		} catch (JMSException e) {
			log.error("Erro ao tentar recuperar o objeto de parametros de consulta!",e);
			throw new ValidadorConsumerException("Erro ao tentar recuperar o objeto de parametros de consulta!",e);
		}
		return consultaOnLineDTO;
	}

	public ValidadorFacade getValidadorFacade() {
		return validadorFacade;
	}

	public void setValidadorFacade(ValidadorFacade validadorFacade) {
		this.validadorFacade = validadorFacade;
	}




	public SwingConsumerMonitor getSwingConsumerMonitor() {
		return swingConsumerMonitor;
	}




	public void setSwingConsumerMonitor(SwingConsumerMonitor swingConsumerMonitor) {
		this.swingConsumerMonitor = swingConsumerMonitor;
	}



	

	public DllDadosDTO getConfiguracaoDll() {
		return configuracaoDll;
	}




	public void setConfiguracaoDll(DllDadosDTO configuracaoDll) {
		this.configuracaoDll = configuracaoDll;
	}


}
