package br.com.dlp.jazzomr.notificacao;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.jazzav.anuncio.AnuncioBusiness;
import br.com.uol.pagseguro.service.NotificationService;
import br.com.uol.pagseguro.service.TransactionSearchService;

/**
 * Servlet para processamento de notificacoes pagseguro
 * 
 * @author darcio
 * 
 */
public class NotificacaoServlet extends HttpServlet {

	protected static final String NOTIFICATION_TYPE = "notificationType";

	protected static final String NOTIFICATION_CODE = "notificationCode";

	private static final long serialVersionUID = 1048086893803996280L;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Objetos e servicos uteis
	 */
	protected ApplicationContext context;
	protected AnuncioBusiness  anuncioBusiness;
	
	protected NotificationService notificationService = new NotificationService();
	protected TransactionSearchService tss = new TransactionSearchService();

	/**
	 * Inicializa servlet
	 */
	@Override
	public void init() throws ServletException {
		context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		anuncioBusiness = (AnuncioBusiness) context.getBean("anuncioBusinessImpl");

		
	}

	/**
	 * Processa o post do Pagseguro para notificar mudanca de estado no processo de pagamento
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String notificationCode = req.getParameter(NOTIFICATION_CODE);
		String notificationType = req.getParameter(NOTIFICATION_TYPE);

		//apenas registrando na saida o que sera realizado 
		PrintWriter pw = new PrintWriter(res.getOutputStream());
		pw.println("Processing notificationCode=" + notificationCode + " notificationType=" + notificationType);

		
		try {
			//aciona metodo de negocio responsavel por registrar a notificacao
			anuncioBusiness.registraNotificacao(notificationCode);
		} catch (JazzBusinessException e) {

			//logando erro e jogando stacktrace na saida do servlet, para facilitar testes e depuracao, sem finalidade pratica para o pagseguro
			log.error("Erro ao tentar registrar notificacao",e);
			e.printStackTrace(pw);
		}
		

		//Registrando sucesso na saida do servlet
		pw.println("Alteracao registrada com sucesso!");
		pw.flush();

	}


}
