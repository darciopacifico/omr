/**
 * 
 */
package br.com.dlp.jazzomr.anuncio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet para receber notificacoes do pagseguro
 * 
 * @author darcio
 *  
 */

public class PagSeguroRetornoServlet extends HttpServlet {

	private static final long serialVersionUID = 7873697147177417626L;

	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			//o método POST indica que a requisição é o retorno da validação NPI.
			Enumeration<?> en = request.getParameterNames();
			String token = "09EE605E38374CB4B23E704CDCD7D8D2";
			StringBuffer validaNPI = new StringBuffer("Comando=validar&Token=");
			validaNPI.append(token);
			
			while (en.hasMoreElements()) {
				String paramName = (String) en.nextElement();
				String paramValue = request.getParameter(paramName);
				validaNPI.append("&");
				validaNPI.append(paramName);
				validaNPI.append("=");
				validaNPI.append(paramValue);
			}

			URL u = new URL("https://pagseguro.uol.com.br/pagseguro-ws/checkout/NPI.jhtml");
			URLConnection uc = u.openConnection();
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.println(validaNPI.toString());
			pw.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String res = in.readLine();
			in.close();

			//Verifica se o status da transação está VERIFICADO
			String transacaoID = request.getParameter("TransacaoID");
			if (res.equals("VERIFICADO")) {
				
				System.out.println("O PAGAMENTO FOI EFETUADO");
				
				
			} else if (res.equals("FALSO")) {
				System.out.println("O PAGAMENTO NNNNNAAAAOOOOOOO FOI EFETUADO");
				
				//o post nao foi validado
			} else {
				System.out.println("ERRO NA INTEGRACAO COM PAGSEGURO");
			}

		} else if ("GET".equalsIgnoreCase(request.getMethod())) {
			//o método GET indica que a requisição é o retorno do Checkout PagSeguro para o site vendedor.
			//no término do checkout o usuário é redirecionado para este bloco.
			
			
			System.out.println("REDIRECIONAR PARA O SITE DO VENDEDOR");
			
			/*
			%>
			<h3>Obrigado por efetuar a compra</h3>
			<%
			*/
		}
		
	}
	
}
