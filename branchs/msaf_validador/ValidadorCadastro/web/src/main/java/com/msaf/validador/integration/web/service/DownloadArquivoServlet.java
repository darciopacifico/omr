package com.msaf.validador.integration.web.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.tratamentoxls.Compactador;
import com.msaf.validador.core.tratamentoxls.GeradorXLSRetorno;
import com.msaf.validador.integration.util.Util;

public class DownloadArquivoServlet extends HttpServlet {

	private static final long serialVersionUID = 7058930749213988641L;

	static ApplicationContext context;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void destroy() {
	}
	
	public ApplicationContext getContext(HttpServletRequest request) {
		if(context == null) {
			ServletContext sc = getServletContext();
		    context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		}
		return context;
	}
	
	
	private void gerarArquivoDownload(ServletRequest request, HttpServletResponse response)throws IOException, FileNotFoundException {
		ApplicationContext context = getContext((HttpServletRequest) request);
		String idPedido = recuperaIdPedido(request);	
		
		OutputStream out = response.getOutputStream();
		response.reset();
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", MessageFormat.format("attachment;filename={0}-validacoes.zip;",idPedido));

		
		try {
			GeradorXLSRetorno geradorXLSRetorno = (GeradorXLSRetorno) context.getBean("geradorXLSRetorno");
			
			ByteArrayOutputStream outArray = new ByteArrayOutputStream();
			
			geradorXLSRetorno.gerarArquivoXLS(Long.valueOf(idPedido), outArray);
			
			Compactador zip = new Compactador();
			
			String name = idPedido + "-validacoes.xls";
			
			out.write(zip.compactar(outArray.toByteArray(), name));
			
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
			out.flush();
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