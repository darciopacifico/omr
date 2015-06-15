package com.msaf.validador.integration.web.service;

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

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.core.lote.GeradorLote;
import com.msaf.validador.integration.hibernate.Repositorio;
import com.msaf.validador.integration.hibernate.intf.IDadosRetInstDAO;
import com.msaf.validador.integration.hibernate.intf.ITpResultDAO;
import com.msaf.validador.integration.util.Util;

public class PaginaRetornoServlet extends HttpServlet {

	private static final long serialVersionUID = 7058930749213988641L;

	static ApplicationContext context;
	
	private Repositorio repositorio;
	
	private String directoryVelocity;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		directoryVelocity = config.getServletContext().getRealPath("");
		
		super.init(config);
	}

	public void destroy() {
	}
	
	public ApplicationContext getContext() {
		
		if(context == null) {
			ServletContext sc = getServletContext();
		    context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		}
		return context;
	}
	
	
	private void gerarPaginaHTML(ServletRequest request, HttpServletResponse response)throws IOException, FileNotFoundException {

		String id = recuperaId(request);	
		
		OutputStream out = response.getOutputStream();
		
		try {
			
			IDadosRetInstDAO dadosRetInstDAO = repositorio.getDadosRetInstDAO();
			
			DadosRetInstVO dadosRetInstVO = dadosRetInstDAO.findById(Long.valueOf(id));
			
			String html = montaHtmlDeRetorno(dadosRetInstVO);
			
			html = html.replace("elt;","<" );
			html = html.replace( "egt;",">");
			html = html.replace("eamp;nbsp;","&nbsp;");	
			byte[] array = html.getBytes();
			out.write(array);
		} finally {
			out.close();
		}
		
	}
	

	private String montaHtmlDeRetorno(DadosRetInstVO dadosRetInstVO) {
		
		if(Util.isEmpty(dadosRetInstVO.getPagina())) {
			
			ApplicationContext context = getContext();
			
			ITpResultDAO tpResultDAO = (ITpResultDAO) context.getBean("tpResultDAO");
			
			String identif = dadosRetInstVO.getIdentif();
			
			if(Util.isEmpty(identif)) {
				return "<html><body><h1>Consulta invalida</h1></body></html>";
			}
			
			TpResultVO tpResultVO = tpResultDAO.findById(Long.parseLong(identif));
			
			return MessageFormat.format("<html><body><h1>{0}</h1></body></html>",tpResultVO.getDescricao());
		}
		
		return dadosRetInstVO.getPagina();
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	private String recuperaId(ServletRequest request) {
		
		String id = request.getParameter("id");
		
		if(Util.isEmpty(id)) {
			throw new IllegalArgumentException("Id pedido vazio!");
		}
		
		return id;
	}
	
	
	/** Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	/** Handles the HTTP POST method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		String dispath = request.getParameter("dispathMethod");
		
		repositorio = (Repositorio)getContext().getBean("repositorio"); 
		
		if (dispath.equals("gerarLote")) {
			
			gerarLote(request, response);
			
		}else {
		
			gerarPaginaHTML(request, response);
		}	
		
		
	}
	
	private void gerarLote(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String id = recuperaId(request);
		
		Long idPessoa = null;
		
		try{
			
			idPessoa = Long.parseLong(id);
			
		}catch (Exception e) {
			throw new IllegalArgumentException("Id Inválido!");
		}
		
		OutputStream out = response.getOutputStream();
		
		response.reset();
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", MessageFormat.format("attachment;filename={0}-certificados.zip;",idPessoa));
		
		
		// cria um gerador de lote
		GeradorLote geradorLote = new GeradorLote(repositorio);
		
		geradorLote.setDirectory(directoryVelocity);
		
		out.write(geradorLote.gerarLotePaginas(idPessoa));
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
	}
	
	/** Returns a short description of the servlet.
	 */
	
	public String getServletInfo() {
		return "Example to create a workbook in a servlet using HSSF";
	}
	
}