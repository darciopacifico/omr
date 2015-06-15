package com.msaf.validador.integration.web.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.tratamentoxls.GeradorXLSRetorno;
import com.msaf.validador.integration.util.Util;

public class DownloadArquivoServlet extends HttpServlet {

	private static final long serialVersionUID = 7058930749213988641L;


	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void destroy() {
	}
	
	public ApplicationContext getContext() {
		String[] CONTEXT_CONFIG_LOCATION = {"src/main/webapp/WEB-INF/action-servlet.xml"};
		ApplicationContext context = new FileSystemXmlApplicationContext(CONTEXT_CONFIG_LOCATION);
		return context;
	}
	
	
	private void gerarArquivoDownload(ServletRequest request, HttpServletResponse response)throws IOException, FileNotFoundException {
		ApplicationContext context = getContext();
		String idPedido = recuperaIdPedido(request);	
		
		OutputStream out = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", MessageFormat.format("attachment;filename={0}-validacoes.xls;",idPedido));

		
		try {
			GeradorXLSRetorno geradorXLSRetorno = (GeradorXLSRetorno) context.getBean("geradorXLSRetorno");
			geradorXLSRetorno.gerarArquivoXLS(Long.valueOf(idPedido), out);
		} catch (LeitorXLSException e) {
			// FIXME: DEVOLVER PARA A TELA DO USUARIO UMA MENSAGEM DE ERRO: EX: ARQUIVO EXCEL INVALIDO
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// FIXME: DEVOLVER PARA A TELA DO USUARIO UMA MENSAGEM DE ERRO: EX: ARQUIVO EXCEL INVALIDO
			e.printStackTrace();
		} catch (Exception e) {
			// FIXME: DEVOLVER PARA A TELA DO USUARIO UMA MENSAGEM DE ERRO: EX: ARQUIVO EXCEL INVALIDO
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	private String recuperaIdPedido(ServletRequest request) {
		String idPedido = request.getParameter("idPedValidacao");
		if(Util.isEmpty(idPedido))   throw new IllegalArgumentException("Id pedido vazio!");
		return idPedido;
	}
	
	
	/** Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//	    	gerarArquivoDownload(request, response);
		this.doPost(request, response);
		
	}
	
	/** Handles the HTTP POST method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		gerarArquivoDownload(request, response);
	}
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
	throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}
	
	/** Returns a short description of the servlet.
	 */
	
	public String getServletInfo() {
		return "Example to create a workbook in a servlet using HSSF";
	}
}